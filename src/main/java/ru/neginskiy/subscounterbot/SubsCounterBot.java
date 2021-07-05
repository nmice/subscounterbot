package ru.neginskiy.subscounterbot;

import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.neginskiy.subscounterbot.botapi.TelegramFacade;

import java.io.InputStream;

/**
 * Класс бота
 */
@Setter
public class SubsCounterBot extends TelegramWebhookBot {
    private final TelegramFacade telegramFacade;
    private String webHookPath;
    private String botUserName;
    private String botToken;

    public SubsCounterBot(DefaultBotOptions botOptions, TelegramFacade telegramFacade) {
        super(botOptions);
        this.telegramFacade = telegramFacade;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotPath() {
        return webHookPath;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return telegramFacade.handleUpdate(update);
    }

    @SneakyThrows
    public void sendPhoto(long chatId, String imageCaption, String imagePath, String imageName) {
        ClassPathResource classPathResource = new ClassPathResource(imagePath);
        InputStream imageInputStream = classPathResource.getInputStream();
        SendPhoto sendPhoto = new SendPhoto().setPhoto(imageName, imageInputStream);
        sendPhoto.setChatId(chatId);
        sendPhoto.setCaption(imageCaption);
        execute(sendPhoto);
    }
}
