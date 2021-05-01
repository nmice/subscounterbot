package ru.neginskiy.subscounterbot.botapi;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

/**
 * Контекст состояний
 */
@Component
public class BotStateContext {
    private Map<BotState, InputMessageHandler> messageHandlers = new HashMap<>();

    public BotStateContext(List<InputMessageHandler> messageHandlers) {
        messageHandlers.forEach(handler -> this.messageHandlers.put(handler.getHandlerName(), handler));
    }

    public SendMessage processInputMessage(BotState currentState, Message message) {
        InputMessageHandler currentMessageHandler = findMessageHandler(currentState);
        return currentMessageHandler.handle(message);
    }

    private InputMessageHandler findMessageHandler(BotState currentState) {
        if (isFillingDataState(currentState)) {
            return messageHandlers.get(BotState.FILLING_PROFILE);
        }
        return messageHandlers.get(currentState);
    }

    private boolean isFillingDataState(BotState currentState) {
        switch (currentState) {
            case ASK_INSTA:
            case ASK_INSTA_LOGIN:
            case ASK_TWITTER:
            case ASK_TWITTER_LOGIN:
            case ASK_YOUTUBE:
            case ASK_YOUTUBE_LOGIN:
            case FILLING_PROFILE:
            case PROFILE_FILLED:
                return true;
            default:
                return false;
        }
    }


}
