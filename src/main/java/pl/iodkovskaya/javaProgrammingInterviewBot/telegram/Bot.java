package pl.iodkovskaya.javaProgrammingInterviewBot.telegram;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import pl.iodkovskaya.javaProgrammingInterviewBot.client.OpenAiClient;
import pl.iodkovskaya.javaProgrammingInterviewBot.repository.TopicRepository;

@Component
public class Bot extends TelegramLongPollingBot {
    private final String INTERVIEW_PROMPT = "Generate a question on the topic: %s";
    private final OpenAiClient openAiClient;
    private final TopicRepository topicRepository;

    public Bot(@Value("${bot.token}") String botToken, OpenAiClient openAiClient, TopicRepository topicRepository) {

        super(botToken);
        this.openAiClient = openAiClient;
        this.topicRepository = topicRepository;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && (update.getMessage().getText()).equals("/start")) {
            String prompt = String.format(INTERVIEW_PROMPT, topicRepository.getRandomTopic());
            String question = openAiClient.promptModel(prompt);
            SendMessage message = new SendMessage();
            message.setChatId(update.getMessage().getChatId());
            message.setText(question);
            try {
                execute(message);
            } catch (TelegramApiException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "Java_Interview_IA_bot";
    }
}
