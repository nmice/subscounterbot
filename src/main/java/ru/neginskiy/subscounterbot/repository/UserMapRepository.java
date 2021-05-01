package ru.neginskiy.subscounterbot.repository;

import org.springframework.stereotype.Repository;
import ru.neginskiy.subscounterbot.model.UserData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Репозиторий данных пользователя в памяти приложения
 */
@Repository
public class UserMapRepository implements UserRepository {
    Map<String, UserData> repository = new HashMap<>();

    @Override
    public List<UserData> findAll() {
        return new ArrayList<>(repository.values());
    }

    @Override
    public void save(UserData userData) {
        repository.put(userData.getId(), userData);
    }

    @Override
    public void deleteById(String chatId) {
        repository.remove(chatId);
    }

    @Override
    public UserData findByChatId(long chatId) {
        return repository.get(String.valueOf(chatId));
    }
}
