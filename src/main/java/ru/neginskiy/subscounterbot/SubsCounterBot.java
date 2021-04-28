package ru.neginskiy.subscounterbot;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class SubsCounterBot extends TelegramWebhookBot {
    private String getBotUsername;
    private String getBotToken;
    private String webHookPath;

    public SubsCounterBot(DefaultBotOptions botOptions) {
        super(botOptions);
    }

    @Override
    public String getBotUsername() {
        return getBotUsername;
    }

    @Override
    public String getBotToken() {
        return getBotToken;
    }

    @Override
    public String getBotPath() {
        return webHookPath;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            long chatId = message.getChatId();
            try {
                execute(new SendMessage(String.valueOf(chatId), "Hi " + update.getMessage().getText()));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void setBotUserName(String getBotUsername) {
        this.getBotUsername = getBotUsername;
    }

    public void setBotToken(String getBotToken) {
        this.getBotToken = getBotToken;
    }

    public void setWebHookPath(String webHookPath) {
        this.webHookPath = webHookPath;
    }
}
