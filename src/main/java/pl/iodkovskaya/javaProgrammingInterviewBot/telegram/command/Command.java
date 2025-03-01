package pl.iodkovskaya.javaProgrammingInterviewBot.telegram.command;

import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;
import pl.iodkovskaya.javaProgrammingInterviewBot.client.OpenAiClient;
import pl.iodkovskaya.javaProgrammingInterviewBot.repository.TopicRepository;
import pl.iodkovskaya.javaProgrammingInterviewBot.telegram.Bot;

@RequiredArgsConstructor
public abstract class Command {
    protected final OpenAiClient openAiClient;
    protected final TopicRepository topicRepository;
    public abstract String process(Update update, Bot bot);
    public abstract boolean isApplicable(Update update);
}
