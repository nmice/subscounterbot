package ru.neginskiy.subscounterbot.cache;

import org.springframework.stereotype.Component;
import ru.neginskiy.subscounterbot.botapi.BotState;
import ru.neginskiy.subscounterbot.model.UserData;

import java.util.HashMap;
import java.util.Map;

/**
 * Реализация кэша в памяти данных пользователя и состояний
 */
@Component
public class InMemoryDataCache implements DataCache {
    private final Map<Integer, BotState> botStateByUserIdMap = new HashMap<>();
    private final Map<Integer, UserData> usersProfileByUserIdMap = new HashMap<>();

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
    public UserData getUserProfileData(int userId) {
        UserData userData = usersProfileByUserIdMap.get(userId);
        if (userData == null) {
            userData = new UserData();
        }
        return userData;
    }

    @Override
    public void saveUserProfileData(int userId, UserData userData) {
        usersProfileByUserIdMap.put(userId, userData);
    }
}
