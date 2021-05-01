package ru.neginskiy.subscounterbot.botapi.handlers.menu;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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
    private ReplyMessagesService messagesService;
    private MainMenuService mainMenuService;

    public MainMenuHandler(ReplyMessagesService messagesService, MainMenuService mainMenuService) {
        this.messagesService = messagesService;
        this.mainMenuService = mainMenuService;
    }

    @Override
    public SendMessage handle(Message message) {
        return mainMenuService.getMainMenuMessage(message.getChatId(),
                messagesService.getReplyText("reply.showMainMenu", Emojis.SCROLL));
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_MAIN_MENU;
    }
}