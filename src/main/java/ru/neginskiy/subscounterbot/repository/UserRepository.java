package ru.neginskiy.subscounterbot.repository;

import ru.neginskiy.subscounterbot.model.UserProfileData;

import java.util.List;

/**
 * Интерфейс репозитория данных пользователя
 */
public interface UserRepository {
    List<UserProfileData> findAll();

    void save(UserProfileData userProfileData);

    void deleteById(String chatId);

    UserProfileData findByChatId(long chatId);
}
