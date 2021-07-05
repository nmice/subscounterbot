package ru.neginskiy.subscounterbot.botapi;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Интерфейс обработчиков (все реализации при инициализации добавляются в контекст состояний)
 */
public interface InputMessageHandler {

    default BotApiMethod<?> processCallBack(CallbackQuery callbackQuery) {
        throw new UnsupportedOperationException();
    }

    SendMessage handle(Message message);

    BotState getHandlerName();
}
