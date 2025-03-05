package pl.iodkovskaya.javaProgrammingInterviewBot.telegram.command;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Voice;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import pl.iodkovskaya.javaProgrammingInterviewBot.client.OpenAiClient;
import pl.iodkovskaya.javaProgrammingInterviewBot.dto.Question;
import pl.iodkovskaya.javaProgrammingInterviewBot.repository.QuestionRepository;
import pl.iodkovskaya.javaProgrammingInterviewBot.repository.TopicRepository;
import pl.iodkovskaya.javaProgrammingInterviewBot.telegram.Bot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Deque;

@Component
public class VoiceCommand extends Command {
    private static final String QUESTION_PROMPT = """
            Here is the initial question for the Java Junior interview in Java: %s
            Ask it by creating an interesting scenario with a well-known real-world application, in the context of which the question will be asked. This way, it won’t just feel like a dry Java question. Think about how to make it more engaging and understandable for the candidate using additional context from a real-world app.
                            
            Keep in mind that the candidate may not know about web applications or complex development yet. However, they are familiar with popular applications such as YouTube, Tinder, Twitter, or Telegram. But avoid using Meta, Facebook, or Instagram as examples. Other apps can be used to create context for your questions to make the interview more interesting for the candidate.
                            
            Example: "Imagine we’re working at Google on the YouTube app. We’re given the task to create a video class with three fields: title, number of likes, and number of views. When adding these fields, it’s important to adhere to the principle of encapsulation in OOP. What is that, and how is it implemented in Java?"
                            
            Feel free to use other famous apps, not just YouTube, as examples. But sometimes YouTube can work too.
            Also, the example above is about encapsulation. However, you should specifically ask a question related to the topic mentioned at the beginning of the prompt. Use the example only for understanding how the question might look in terms of style and context, but the content should directly relate to the chosen topic.
                            
            Communication style:
            Talk to the candidate informally, like two good friends having a warm and casual conversation, without unnecessary formalities.
            Avoid overly wordy phrasing. Try to keep sentences clear and to the point while maintaining the warmth and liveliness of the conversation. Find the right balance. However, the person shouldn’t get an enormous text that would make them lazy to read. The conversation should be interesting but concise, so they don’t lose the desire to continue because of the large amount of text on the screen.
            """;
    private static final String FEEDBACK_PROMPT = """
                Analyze the interview questions for the Junior Java Developer position and the candidate's responses to each of them. Provide feedback on how well the candidate answered these questions at this level of the interview.
                
                Each question corresponds to the answer that follows it. The questions may be unrelated, so evaluate each response individually rather than considering them as a whole.
                
                When giving feedback, start by highlighting what the candidate did well, which topics they have a deep understanding of, and where they demonstrated a strong level of knowledge.
                
                Next, focus on the answers that were incorrect or only partially correct. Clearly indicate where the candidate made mistakes in their statements and explain how they could improve their response. Provide detailed feedback in this section so that the candidate clearly understands their errors and can learn from them.
                
                Then, suggest which topics the candidate should focus on more during further preparation to fill the gaps identified during the interview analysis.
                
                Tone of communication:
                Speak to the candidate in a friendly and informal manner, as if it were a conversation between two good friends—warm and relaxed, without unnecessary formalities.
                
                Avoid overly wordy formulations. Keep sentences clear and to the point while maintaining a cozy and engaging conversation. The text should not be overwhelming or tedious to read. The conversation should be interesting yet concise to keep the candidate engaged.
                
                Formatting:
                Separate feedback into paragraphs every 4-5 sentences. Use distinct headings for the sections:
                
                What Went Well
                What Can Be Improved
                What to Focus On
                Interview results:                        
            """;
    @Value("${interview.max-questions}")
    private int maxQuestions;

    public VoiceCommand(OpenAiClient openAiClient, TopicRepository topicRepository, QuestionRepository questionRepository) {
        super(openAiClient, topicRepository, questionRepository);
    }

    @Override
    public String process(Update update, Bot bot) {
        String answer = transcribeVoiceAnswer(update, bot);
        String userName = update.getMessage().getFrom().getUserName();
        questionRepository.addAnswer(userName, answer);

        if (questionRepository.getUserQuestions(userName) == maxQuestions) {
            return provideFeedback(userName);
        } else {
            return askNextQuestion(userName);
        }
    }

    @Override
    public boolean isApplicable(Update update) {

        return update.hasMessage() && update.getMessage().hasVoice();
    }

    private String transcribeVoiceAnswer(Update update, Bot bot) {
        Voice voice = update.getMessage().getVoice();
        String fileId = voice.getFileId();
        java.io.File audio;
        try {
            GetFile getFileRequest = new GetFile();
            getFileRequest.setFileId(fileId);
            File file = bot.execute(getFileRequest);

            audio = bot.downloadFile(file.getFilePath());
        } catch (TelegramApiException e) {
            throw new IllegalStateException("There's an error when processing Telegram audio", e);
        }
        return openAiClient.transcribe(renameToOgg(audio));
    }

    private java.io.File renameToOgg(java.io.File tmpFile) {
        String fileName = tmpFile.getName();
        String newFileName = fileName.substring(0, fileName.length() - 4) + ".ogg";
        Path sourcePath = tmpFile.toPath();
        Path targetPath = sourcePath.resolveSibling(newFileName);
        try {
            Files.move(sourcePath, targetPath);
        } catch (IOException e) {
            throw new IllegalStateException("There was an error when renaming .tmp audio file to .ogg", e);
        }
        return targetPath.toFile();
    }
    private String askNextQuestion(String userName) {
        String prompt = String.format(QUESTION_PROMPT, topicRepository.getRandomTopic());
        String question = openAiClient.promptModel(prompt);
        questionRepository.addQuestion(userName, question);
        return question;
    }

    private String provideFeedback(String userName) {
        StringBuilder feedbackPrompt = new StringBuilder();
        feedbackPrompt.append(FEEDBACK_PROMPT);
        Deque<Question> questions = questionRepository.finishInterview(userName);
        questions.forEach(question -> feedbackPrompt.append("Original question: ")
                .append(question.getQuestion()).append("\n")
                .append("Candidate's answer: ")
                .append(question.getAnswer()).append("\n"));
        return openAiClient.promptModel(feedbackPrompt.toString());
    }
}
