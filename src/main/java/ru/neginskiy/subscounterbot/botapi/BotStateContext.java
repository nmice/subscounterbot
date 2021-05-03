package ru.neginskiy.subscounterbot.botapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Контекст состояний
 */
@Component
@Slf4j
public class BotStateContext {
    private Map<BotState, InputMessageHandler> messageHandlers = new HashMap<>();

    public BotStateContext(List<InputMessageHandler> messageHandlers) {
        messageHandlers.forEach(handler -> this.messageHandlers.put(handler.getHandlerName(), handler));
    }

    public BotApiMethod<?> processInputMessage(BotState currentState, BotApiObject botApiObject) {
        InputMessageHandler currentMessageHandler = findMessageHandler(currentState);
        if (botApiObject instanceof Message) {
            return currentMessageHandler.handle((Message) botApiObject);
        } else if (botApiObject instanceof CallbackQuery) {
            return currentMessageHandler.processCallBack((CallbackQuery) botApiObject);
        } else {
            log.error("Illegal input type");
            throw new IllegalArgumentException();
        }
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
