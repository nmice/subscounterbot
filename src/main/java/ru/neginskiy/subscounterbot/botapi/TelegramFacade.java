package ru.neginskiy.subscounterbot.botapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.neginskiy.subscounterbot.SubsCounterBot;
import ru.neginskiy.subscounterbot.cache.DataCache;
import ru.neginskiy.subscounterbot.service.ButtonsProvider;
import ru.neginskiy.subscounterbot.service.MainMenuService;
import ru.neginskiy.subscounterbot.service.ReplyMessagesService;

/**
 * Фасад для доступа к обработчикам для update запросов и обработки callback-запросов
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
        BotApiMethod<?> replyMessage = null;
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            log.info("New callbackQuery from User: {}, userId: {}, with data: {}", update.getCallbackQuery().getFrom().getUserName(),
                    callbackQuery.getFrom().getId(), update.getCallbackQuery().getData());
            replyMessage = handleCallbackQuery(callbackQuery);
        }
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            log.info("New message from User:{}, userId: {}, chatId: {},  with text: {}",
                    message.getFrom().getUserName(), message.getFrom().getId(), message.getChatId(), message.getText());
            replyMessage = handleInputMessage(message);
        }
        return replyMessage;
    }

    private BotApiMethod<?> handleInputMessage(Message message) {
        String inputMsg = message.getText();
        int userId = message.getFrom().getId();
        long chatId = message.getChatId();

        BotState botState;
        switch (inputMsg) {
            case "/start":
                subsCounterBot.sendPhoto(chatId,
                        messagesService.getReplyText("reply.hello"),
                        "static/images/subscounterbot.jpg",
                        "subscounterbot");
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
        return botStateContext.processInputMessage(botState, message);
    }

    private BotApiMethod<?> handleCallbackQuery(CallbackQuery buttonQuery) {
        String inputMsg = buttonQuery.getData();
        int userId = buttonQuery.getFrom().getId();

        BotState botState;
        switch (inputMsg) {
            case "buttonYes":
            case "buttonNo":
                botState = BotState.ASK_READY;
                break;
            case "InstaYes":
            case "InstaNo":
                botState = BotState.ASK_INSTA;
                break;
            case "TwitterYes":
            case "TwitterNo":
                botState = BotState.ASK_TWITTER;
                break;
            case "YouTubeYes":
            case "YouTubeNo":
                botState = BotState.ASK_YOUTUBE;
                break;
            default:
                botState = userDataCache.getUsersCurrentBotState(userId);
                break;
        }
        userDataCache.setUsersCurrentBotState(userId, botState);
        return botStateContext.processInputMessage(botState, buttonQuery);
    }
}