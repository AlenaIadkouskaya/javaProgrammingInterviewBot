package pl.iodkovskaya.javaProgrammingInterviewBot.telegram;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import pl.iodkovskaya.javaProgrammingInterviewBot.client.OpenAiClient;
import pl.iodkovskaya.javaProgrammingInterviewBot.repository.TopicRepository;
import pl.iodkovskaya.javaProgrammingInterviewBot.telegram.command.Command;
import pl.iodkovskaya.javaProgrammingInterviewBot.telegram.command.StartCommand;

import java.util.List;

@Component
public class Bot extends TelegramLongPollingBot {

    private final List<Command> commands;


    public Bot(@Value("${bot.token}") String botToken, List<Command> commands) {

        super(botToken);
        this.commands = commands;
    }

    @Override
    public void onUpdateReceived(Update update) {

        commands.stream()
                .filter(command -> command.isApplicable(update))
                .findFirst()
                .ifPresent(command -> {
                    String question = command.process(update);
                    SendMessage message = new SendMessage();
                    message.setChatId(update.getMessage().getChatId());
                    message.setText(question);
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new IllegalStateException(e);
                    }
                });
    }

    @Override
    public String getBotUsername() {
        return "Java_Interview_IA_bot";
    }
}
