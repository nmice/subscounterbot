package ru.neginskiy.subscounterbot.cache;

import ru.neginskiy.subscounterbot.botapi.BotState;
import ru.neginskiy.subscounterbot.model.UserData;

/**
 * Интерфейс кэша данных пользователя и состояний
 */
public interface DataCache {
    void setUsersCurrentBotState(int userId, BotState botState);

    BotState getUsersCurrentBotState(int userId);

    UserData getUserProfileData(int userId);

    void saveUserProfileData(int userId, UserData userData);
}