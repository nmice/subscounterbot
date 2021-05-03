package ru.neginskiy.subscounterbot.botapi.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.neginskiy.subscounterbot.botapi.BotState;
import ru.neginskiy.subscounterbot.botapi.InputMessageHandler;
import ru.neginskiy.subscounterbot.cache.DataCache;
import ru.neginskiy.subscounterbot.service.ButtonsProvider;
import ru.neginskiy.subscounterbot.service.ReplyMessagesService;

/**
 * Обработчик состояния - ASK_READY
 */
@Slf4j
@Component
public class AskReadyHandler implements InputMessageHandler {
    private DataCache userDataCache;
    private ReplyMessagesService messagesService;
    private ButtonsProvider buttonsProvider;

    public AskReadyHandler(DataCache userDataCache, ReplyMessagesService messagesService, ButtonsProvider buttonsProvider) {
        this.messagesService = messagesService;
        this.buttonsProvider = buttonsProvider;
        this.userDataCache = userDataCache;
    }

    @Override
    public SendMessage handle(Message message) {
        long chatId = message.getChatId();
        SendMessage replyToUser = messagesService.getReplyMessageFromLocale(chatId, "reply.askReady");
        replyToUser.setReplyMarkup(buttonsProvider.getYesNoButtonsMarkup("buttonYes", "buttonNo"));
        return replyToUser;
    }

    @Override
    public BotApiMethod<?> processCallBack(CallbackQuery buttonQuery) {
        BotApiMethod<?> callBackAnswer = null;
        long chatId = buttonQuery.getMessage().getChatId();
        int userId = buttonQuery.getFrom().getId();
        if (buttonQuery.getData().equals("buttonYes")) {
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_INSTA);
            SendMessage replyToUser = messagesService.getReplyMessageFromLocale(chatId, "reply.askInsta");
            replyToUser.setReplyMarkup(buttonsProvider.getYesNoButtonsMarkup("InstaYes", "InstaNo"));
            callBackAnswer = replyToUser;
        } else if (buttonQuery.getData().equals("buttonNo")) {
            AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
            answerCallbackQuery.setCallbackQueryId(buttonQuery.getId());
            answerCallbackQuery.setShowAlert(false);
            answerCallbackQuery.setText("Возвращайся, когда будешь готов");
            callBackAnswer = answerCallbackQuery;
        }
        return callBackAnswer;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ASK_READY;
    }
}
