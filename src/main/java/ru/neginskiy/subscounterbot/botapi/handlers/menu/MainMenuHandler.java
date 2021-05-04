package ru.neginskiy.subscounterbot.botapi.handlers.menu;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.neginskiy.subscounterbot.botapi.BotState;
import ru.neginskiy.subscounterbot.botapi.InputMessageHandler;
import ru.neginskiy.subscounterbot.service.MainMenuService;
import ru.neginskiy.subscounterbot.service.ReplyMessagesService;
import ru.neginskiy.subscounterbot.utils.Emojis;

/**
 * Обработчик любых текстовых запросов из шага выбора меню
 */
@Component
public class MainMenuHandler implements InputMessageHandler {
    private final ReplyMessagesService messagesService;
    private final MainMenuService mainMenuService;

    public MainMenuHandler(ReplyMessagesService messagesService, MainMenuService mainMenuService) {
        this.messagesService = messagesService;
        this.mainMenuService = mainMenuService;
    }

    @Override
    public SendMessage handle(Message message) {
        long chatId = message.getChatId();
        return getMainMenuMessage(chatId);
    }

    @Override
    public BotApiMethod<?> processCallBack(CallbackQuery buttonQuery) {
        long chatId = buttonQuery.getMessage().getChatId();
        return getMainMenuMessage(chatId);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_MAIN_MENU;
    }

    private SendMessage getMainMenuMessage(long chatId) {
        return mainMenuService.getMainMenuMessage(chatId,
                messagesService.getReplyText("reply.showMainMenu", Emojis.SCROLL));
    }
}