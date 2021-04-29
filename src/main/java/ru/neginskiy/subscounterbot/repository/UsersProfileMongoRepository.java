package ru.neginskiy.subscounterbot.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.neginskiy.subscounterbot.model.UserProfileData;

@Repository
public interface UsersProfileMongoRepository extends MongoRepository<UserProfileData, String> {
    UserProfileData findByChatId(long chatId);
    void deleteByChatId(long chatId);
}
