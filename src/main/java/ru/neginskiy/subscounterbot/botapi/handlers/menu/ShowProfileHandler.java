package ru.neginskiy.subscounterbot.botapi.handlers.menu;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.neginskiy.subscounterbot.botapi.BotState;
import ru.neginskiy.subscounterbot.botapi.InputMessageHandler;
import ru.neginskiy.subscounterbot.cache.DataCache;
import ru.neginskiy.subscounterbot.model.UserData;
import ru.neginskiy.subscounterbot.service.UsersDataService;

/**
 * Обработчик запроса данных по кнопке "Мои аккаунты"
 */
@Component
public class ShowProfileHandler implements InputMessageHandler {
    private final DataCache userDataCache;
    private final UsersDataService userDataService;

    public ShowProfileHandler(DataCache userDataCache, UsersDataService userDataService) {
        this.userDataCache = userDataCache;
        this.userDataService = userDataService;
    }

    @Override
    public SendMessage handle(Message message) {
        int userId = message.getFrom().getId();
        UserData userData = userDataService.getUserProfileData(message.getChatId());
        userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
        String description;
        if (userData != null) {
            description = String.format("%s%n-------------------%n%s", "Данные по вашей анкете:",
                    userData.toString());
        } else {
            description = "Такой анкеты в БД не существует !";
        }
        return new SendMessage(message.getChatId(), description);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_USER_SOCIAL_MEDIA_STATS;
    }
}