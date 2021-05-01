package ru.neginskiy.subscounterbot.cache;

import org.springframework.stereotype.Component;
import ru.neginskiy.subscounterbot.botapi.BotState;
import ru.neginskiy.subscounterbot.model.UserProfileData;

import java.util.HashMap;
import java.util.Map;

/**
 * Реализация кэша в памяти
 * usersBotStates: user_id and user's bot state
 * usersProfileData: user_id  and user's profile data.
 * данных пользователя и состояний
 */
@Component
public class InMemoryDataCache implements DataCache {
    private Map<Integer, BotState> botStateByUserIdMap = new HashMap<>();
    private Map<Integer, UserProfileData> usersProfileByUserIdMap = new HashMap<>();

    @Override
    public void setUsersCurrentBotState(int userId, BotState botState) {
        botStateByUserIdMap.put(userId, botState);
    }

    @Override
    public BotState getUsersCurrentBotState(int userId) {
        BotState botState = botStateByUserIdMap.get(userId);
        if (botState == null) {
            botState = BotState.ASK_READY;
        }
        return botState;
    }

    @Override
    public UserProfileData getUserProfileData(int userId) {
        UserProfileData userProfileData = usersProfileByUserIdMap.get(userId);
        if (userProfileData == null) {
            userProfileData = new UserProfileData();
        }
        return userProfileData;
    }

    @Override
    public void saveUserProfileData(int userId, UserProfileData userProfileData) {
        usersProfileByUserIdMap.put(userId, userProfileData);
    }
}
