package ru.neginskiy.subscounterbot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.neginskiy.subscounterbot.SubsCounterBot;

import java.time.LocalDateTime;

/**
 * Контроллер для обработки запроса от telegram-клиента
 */
@RestController
@Slf4j
public class WebHookController {
    private final SubsCounterBot telegramBot;

    public WebHookController(SubsCounterBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @PostMapping("/")
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return telegramBot.onWebhookUpdateReceived(update);
    }

    @Scheduled(fixedDelay = 60000)
    public void wakeUp() {
        log.info("WAKE UP,LAZY BOT! localDateTime: {} ;-)", LocalDateTime.now());
    }
}
