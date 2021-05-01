package ru.neginskiy.subscounterbot.botapi.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.neginskiy.subscounterbot.botapi.BotState;
import ru.neginskiy.subscounterbot.botapi.InputMessageHandler;
import ru.neginskiy.subscounterbot.service.ReplyMessagesService;
import ru.neginskiy.subscounterbot.service.ButtonsProvider;

/**
 * Обработчик состояния - ASK_READY
 */
@Slf4j
@Component
public class AskReadyHandler implements InputMessageHandler {
    private ReplyMessagesService messagesService;
    private ButtonsProvider buttonsProvider;

    public AskReadyHandler(ReplyMessagesService messagesService, ButtonsProvider buttonsProvider) {
        this.messagesService = messagesService;
        this.buttonsProvider = buttonsProvider;
    }

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ASK_READY;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        long chatId = inputMsg.getChatId();
        SendMessage replyToUser = messagesService.getReplyMessageFromLocale(chatId, "reply.askReady");
        replyToUser.setReplyMarkup(buttonsProvider.getFourInlineMessageButtons());
        return replyToUser;
    }
}
