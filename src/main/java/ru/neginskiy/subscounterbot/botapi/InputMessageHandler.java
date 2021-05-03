package ru.neginskiy.subscounterbot.botapi;

import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Интерфейс обработчиков (все реализации при инициализации добавляются в контекст состояний)
 */
public interface InputMessageHandler {
    SendMessage handle(Message message);

    default BotApiMethod<?> processCallBack(CallbackQuery callbackQuery){
        return null;
    }

    BotState getHandlerName();
}
