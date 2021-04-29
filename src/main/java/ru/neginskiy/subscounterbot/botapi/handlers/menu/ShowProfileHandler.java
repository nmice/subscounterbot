package ru.neginskiy.subscounterbot.botapi.handlers.menu;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.neginskiy.subscounterbot.botapi.BotState;
import ru.neginskiy.subscounterbot.botapi.InputMessageHandler;
import ru.neginskiy.subscounterbot.model.UserProfileData;
import ru.neginskiy.subscounterbot.cache.UserDataCache;
import ru.neginskiy.subscounterbot.service.UsersProfileDataService;

@Component
public class ShowProfileHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private UsersProfileDataService profileDataService;

    public ShowProfileHandler(UserDataCache userDataCache, UsersProfileDataService profileDataService) {
        this.userDataCache = userDataCache;
        this.profileDataService = profileDataService;
    }

    @Override
    public SendMessage handle(Message message) {
        SendMessage userReply;
        final int userId = message.getFrom().getId();
        final UserProfileData profileData = profileDataService.getUserProfileData(message.getChatId());

        userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
        if (profileData != null) {
            userReply = new SendMessage(message.getChatId(),
                    String.format("%s%n-------------------%n%s", "Данные по вашей анкете:", profileData.toString()));
        } else {
            userReply = new SendMessage(message.getChatId(), "Такой анкеты в БД не существует !");
        }

        return userReply;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_USER_PROFILE;
    }
}