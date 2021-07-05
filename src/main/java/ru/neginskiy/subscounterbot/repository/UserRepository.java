package ru.neginskiy.subscounterbot.repository;

import ru.neginskiy.subscounterbot.model.UserData;

import java.util.List;

/**
 * Интерфейс репозитория данных пользователя
 */
public interface UserRepository {
    List<UserData> findAll();

    void save(UserData userData);

    void deleteById(String chatId);

    UserData findByChatId(long chatId);
}
