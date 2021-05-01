package ru.neginskiy.subscounterbot.service;

import org.springframework.stereotype.Service;
import ru.neginskiy.subscounterbot.model.UserData;
import ru.neginskiy.subscounterbot.repository.UserRepository;

import java.util.List;

/**
 * Сервис работы с данными пользователя
 */
@Service
public class UsersProfileDataService {

    UserRepository repository;

    public UsersProfileDataService(UserRepository repository) {
        this.repository = repository;
    }

    public List<UserData> getAllProfiles() {
        return repository.findAll();
    }

    public void saveUserProfileData(UserData userData) {
        repository.save(userData);
    }

    public void deleteUsersProfileData(String profileDataId) {
        repository.deleteById(profileDataId);
    }

    public UserData getUserProfileData(long chatId) {
        return repository.findByChatId(chatId);
    }
}
