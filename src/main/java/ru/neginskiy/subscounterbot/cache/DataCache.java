package ru.neginskiy.subscounterbot.cache;

import ru.neginskiy.subscounterbot.botapi.BotState;
import ru.neginskiy.subscounterbot.model.UserProfileData;

public interface DataCache {
    void setUsersCurrentBotState(int userId, BotState botState);

    BotState getUsersCurrentBotState(int userId);

    UserProfileData getUserProfileData(int userId);

    void saveUserProfileData(int userId, UserProfileData userProfileData);
}