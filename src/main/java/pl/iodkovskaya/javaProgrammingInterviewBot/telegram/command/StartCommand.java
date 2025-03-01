package pl.iodkovskaya.javaProgrammingInterviewBot.telegram.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import pl.iodkovskaya.javaProgrammingInterviewBot.client.OpenAiClient;
import pl.iodkovskaya.javaProgrammingInterviewBot.repository.TopicRepository;
import pl.iodkovskaya.javaProgrammingInterviewBot.telegram.Bot;

@Component
public class StartCommand extends Command {
    private final String INTERVIEW_PROMPT = "Welcome the candidate to the interview in a fun and warm way, and also explain the interview rules:\n" +
            "\n" +
            "There will be 10 Java questions at the Junior level.\n" +
            "The questions will be about Java Core, and only about it.\n" +
            "The candidate can answer the questions using voice messages and as detailed as they think necessary.\n" +
            "After the 10 answers, feedback will be provided by the interviewer, highlighting strengths and also areas where the answers were not very accurate, with suggestions on which aspects the candidate should focus on during their preparation.\n" +
            "Make sure to follow these rules when asking questions. Keep in mind that the candidate might not know about web applications or complex development yet. However, they are familiar with popular applications like YouTube, Tinder, Twitter, or Telegram. Avoid using Meta, Facebook, or Instagram as examples. Other applications can be used to create context for the questions to make the interview more interesting for the candidate.\n" +
            "\n" +
            "Here’s an example of a Java Junior interview question: %s\n" +
            "Ask it by creating an interesting scenario with a real, well-known app, where the question will be asked. Make it not just a dry Java question but an engaging and understandable one for the candidate, using additional context from a real app.\n" +
            "\n" +
            "For example: \"Imagine we're working at Google on the YouTube app. We're given a task to create a video class with three fields: title, number of likes, and number of views. When adding these fields, it's important to adhere to the principle of encapsulation in OOP. What is that, and how is it implemented in Java?\"\n" +
            "\n" +
            "Feel free to use other well-known apps as examples, not just YouTube. But sometimes, YouTube can work too.\n" +
            "\n" +
            "Communication style:\n" +
            "Talk to the candidate informally, like two good friends having a casual conversation—warm, easy-going, and without unnecessary formalities.\n" +
            "Avoid overly wordy phrasing. Try to keep the sentences clear and to the point, while maintaining the warmth and liveliness of the conversation. It's important to find a balance. The person should not be overwhelmed by long texts that are tedious to read. The conversation should be interesting and concise, so they won’t lose interest due to a lot of text on the screen.\n" +
            "\n";

    public StartCommand(OpenAiClient openAiClient, TopicRepository topicRepository) {
        super(openAiClient, topicRepository);
    }

    public String process(Update update, Bot bot) {
        String prompt = String.format(INTERVIEW_PROMPT, topicRepository.getRandomTopic());
        String question = openAiClient.promptModel(prompt);
        return question;
    }

    public boolean isApplicable(Update update) {
        Message message = update.getMessage();
        return message.hasText() && "/start".equals(message.getText());
    }
}
