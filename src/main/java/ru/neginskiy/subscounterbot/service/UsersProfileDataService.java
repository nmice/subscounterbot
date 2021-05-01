package ru.neginskiy.subscounterbot.service;

import org.springframework.stereotype.Service;
import ru.neginskiy.subscounterbot.model.UserProfileData;
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

    public List<UserProfileData> getAllProfiles() {
        return repository.findAll();
    }

    public void saveUserProfileData(UserProfileData userProfileData) {
        repository.save(userProfileData);
    }

    public void deleteUsersProfileData(String profileDataId) {
        repository.deleteById(profileDataId);
    }

    public UserProfileData getUserProfileData(long chatId) {
        return repository.findByChatId(chatId);
    }
}
