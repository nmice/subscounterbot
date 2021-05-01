package ru.neginskiy.subscounterbot.repository;

import org.springframework.stereotype.Repository;
import ru.neginskiy.subscounterbot.model.UserProfileData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Репозиторий данных пользователя в памяти приложения
 */
@Repository
public class UserMapRepository implements UserRepository {
    Map<String, UserProfileData> repository = new HashMap<>();

    @Override
    public List<UserProfileData> findAll() {
        return new ArrayList<>(repository.values());
    }

    @Override
    public void save(UserProfileData userProfileData) {
        repository.put(userProfileData.getId(), userProfileData);
    }

    @Override
    public void deleteById(String chatId) {
        repository.remove(chatId);
    }

    @Override
    public UserProfileData findByChatId(long chatId) {
        return repository.get(String.valueOf(chatId));
    }
}
