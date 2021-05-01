package ru.neginskiy.subscounterbot.botapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.neginskiy.subscounterbot.SubsCounterBot;
import ru.neginskiy.subscounterbot.cache.DataCache;
import ru.neginskiy.subscounterbot.service.MainMenuService;
import ru.neginskiy.subscounterbot.service.ReplyMessagesService;
import ru.neginskiy.subscounterbot.service.ButtonsProvider;

/**
 * Фасад для доступа к обработчикам и определения типа запроса
 */
@Component
@Slf4j
public class TelegramFacade {
    private BotStateContext botStateContext;
    private DataCache userDataCache;
    private MainMenuService mainMenuService;
    private SubsCounterBot subsCounterBot;
    private ReplyMessagesService messagesService;
    private ButtonsProvider buttonsProvider;

    public TelegramFacade(BotStateContext botStateContext, DataCache userDataCache,
                          MainMenuService mainMenuService, @Lazy SubsCounterBot subsCounterBot,
                          ReplyMessagesService messagesService, ButtonsProvider buttonsProvider) {
        this.botStateContext = botStateContext;
        this.userDataCache = userDataCache;
        this.mainMenuService = mainMenuService;
        this.subsCounterBot = subsCounterBot;
        this.messagesService = messagesService;
        this.buttonsProvider = buttonsProvider;
    }

    public BotApiMethod<?> handleUpdate(Update update) {
        SendMessage replyMessage = null;

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            log.info("New callbackQuery from User: {}, userId: {}, with data: {}", update.getCallbackQuery().getFrom().getUserName(),
                    callbackQuery.getFrom().getId(), update.getCallbackQuery().getData());
            return processCallbackQuery(callbackQuery);
        }
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            log.info("New message from User:{}, userId: {}, chatId: {},  with text: {}",
                    message.getFrom().getUserName(), message.getFrom().getId(), message.getChatId(), message.getText());
            replyMessage = handleInputMessage(message);
        }
        return replyMessage;
    }

    private SendMessage handleInputMessage(Message message) {
        String inputMsg = message.getText();
        int userId = message.getFrom().getId();
        long chatId = message.getChatId();
        BotState botState;
        SendMessage replyMessage;

        switch (inputMsg) {
            case "/start":
                subsCounterBot.sendPhoto(chatId, messagesService.getReplyText("reply.hello"), "static/images/subscounterbot.jpg");
                botState = BotState.ASK_READY;
                break;
            case "Получить инфо по аккаунту":
                botState = BotState.ASK_INSTA;
                break;
            case "Мои аккаунты":
                botState = BotState.SHOW_USER_SOCIAL_MEDIA_STATS;
                break;
            case "Помощь":
                botState = BotState.SHOW_HELP_MENU;
                break;
            default:
                botState = userDataCache.getUsersCurrentBotState(userId);
                break;
        }

        userDataCache.setUsersCurrentBotState(userId, botState);
        replyMessage = botStateContext.processInputMessage(botState, message);
        return replyMessage;
    }

    private BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery) {
        final long chatId = buttonQuery.getMessage().getChatId();
        final int userId = buttonQuery.getFrom().getId();
        BotApiMethod<?> callBackAnswer = mainMenuService.getMainMenuMessage(chatId, "Воспользуйтесь главным меню");

        // Обработка ответа по кнопкам на стартовом экране
        if (buttonQuery.getData().equals("buttonYes")) {
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_INSTA);
            SendMessage replyToUser = messagesService.getReplyMessageFromLocale(chatId, "reply.askInsta");
            replyToUser.setReplyMarkup(buttonsProvider.getYesNoButtonsMarkup("InstaYes", "InstaNo"));
            callBackAnswer = replyToUser;
        } else if (buttonQuery.getData().equals("buttonNo")) {
            callBackAnswer = sendAnswerCallbackQuery("Возвращайся, когда будешь готов", false, buttonQuery);
        } else if (buttonQuery.getData().equals("buttonIwillThink")) {
            callBackAnswer = sendAnswerCallbackQuery("Данная кнопка не поддерживается", true, buttonQuery);
        }

        // Обработка ответа по кнопкам на экране Insta
        else if (buttonQuery.getData().equals("InstaYes")) {
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_INSTA_LOGIN);
            callBackAnswer = messagesService.getReplyMessageFromLocale(chatId, "reply.askInstaLogin");
        } else if (buttonQuery.getData().equals("InstaNo")) {
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_TWITTER);
            SendMessage replyToUser = messagesService.getReplyMessageFromLocale(chatId, "reply.askTwitter");
            replyToUser.setReplyMarkup(buttonsProvider.getYesNoButtonsMarkup("TwitterYes", "TwitterNo"));
            callBackAnswer = replyToUser;
        }

        // Обработка ответа по кнопкам на экране Twitter
        else if (buttonQuery.getData().equals("TwitterYes")) {
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_TWITTER_LOGIN);
            callBackAnswer = messagesService.getReplyMessageFromLocale(chatId, "reply.askTwitterLogin");
        } else if (buttonQuery.getData().equals("TwitterNo")) {
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_YOUTUBE);
            SendMessage replyToUser = messagesService.getReplyMessageFromLocale(chatId, "reply.askYouTube");
            replyToUser.setReplyMarkup(buttonsProvider.getYesNoButtonsMarkup("YouTubeYes", "YouTubeNo"));
            callBackAnswer = replyToUser;
        }

        // Обработка ответа по кнопкам на экране YouTube
        else if (buttonQuery.getData().equals("YouTubeYes")) {
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_YOUTUBE_LOGIN);
            callBackAnswer = messagesService.getReplyMessageFromLocale(chatId, "reply.askYouTubeLogin");
        } else if (buttonQuery.getData().equals("YouTubeNo")) {
            userDataCache.setUsersCurrentBotState(userId, BotState.PROFILE_FILLED);
            callBackAnswer = messagesService.getReplyMessageFromLocale(chatId, "reply.profileFilled");
        } else {
            userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
        }
        return callBackAnswer;
    }

    private AnswerCallbackQuery sendAnswerCallbackQuery(String text, boolean alert, CallbackQuery callbackquery) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackquery.getId());
        answerCallbackQuery.setShowAlert(alert);
        answerCallbackQuery.setText(text);
        return answerCallbackQuery;
    }
}