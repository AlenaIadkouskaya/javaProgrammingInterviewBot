package pl.iodkovskaya.javaProgrammingInterviewBot.telegram.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class VoiceCommand extends Command{
    @Override
    public String process(Update update) {
        return null;
    }

    @Override
    public boolean isApplicable(Update update) {
        return false;
    }
}
