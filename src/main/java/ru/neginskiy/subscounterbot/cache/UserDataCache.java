package ru.neginskiy.subscounterbot.cache;

import org.springframework.stereotype.Component;
import ru.neginskiy.subscounterbot.botapi.BotState;
import ru.neginskiy.subscounterbot.botapi.handlers.fillingprofile.UserProfileData;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserDataCache implements DataCache {
    private Map<Integer, BotState> usersBotStates = new HashMap<>();
    private Map<Integer, UserProfileData> usersProfileData = new HashMap<>();

    @Override
    public void setUsersCurrentBotState(int userId, BotState botState) {
        usersBotStates.put(userId, botState);
    }

    @Override
    public BotState getUsersCurrentBotState(int userId) {
        BotState botState = usersBotStates.get(userId);
        if (botState == null) {
            botState = BotState.ASK_DESTINY;
        }

        return botState;
    }

    @Override
    public UserProfileData getUserProfileData(int userId) {
        UserProfileData userProfileData = usersProfileData.get(userId);
        if (userProfileData == null) {
            userProfileData = new UserProfileData();
        }
        return userProfileData;
    }

    @Override
    public void saveUserProfileData(int userId, UserProfileData userProfileData) {
        usersProfileData.put(userId, userProfileData);
    }
}
