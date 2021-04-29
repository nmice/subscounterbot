package ru.neginskiy.subscounterbot.service;

import org.springframework.stereotype.Service;
import ru.neginskiy.subscounterbot.model.UserProfileData;
import ru.neginskiy.subscounterbot.repository.UsersProfileMongoRepository;

import java.util.List;

@Service
public class UsersProfileDataService {

    private UsersProfileMongoRepository profileMongoRepository;

    public UsersProfileDataService(UsersProfileMongoRepository profileMongoRepository) {
        this.profileMongoRepository = profileMongoRepository;
    }

    public List<UserProfileData> getAllProfiles() {
        return profileMongoRepository.findAll();
    }

    public void saveUserProfileData(UserProfileData userProfileData) {
        profileMongoRepository.save(userProfileData);
    }

    public void deleteUsersProfileData(String profileDataId) {
        profileMongoRepository.deleteById(profileDataId);
    }

    public UserProfileData getUserProfileData(long chatId) {
        return profileMongoRepository.findByChatId(chatId);
    }
}
