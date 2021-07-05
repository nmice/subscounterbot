package ru.neginskiy.subscounterbot.service.socialmedia;


import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Сервис по работе с Instagram API через Instagram4j
 */
@Service
public class InstagramService implements SocialMediaService{
    private final String instaLogin;
    private final String instaPassword;

    public InstagramService(@Value("${social.instaLogin}") String instaLogin,
                            @Value("${social.instaPassword}") String instaPassword) {
        this.instaLogin = instaLogin;
        this.instaPassword = instaPassword;
    }

    public String getSubsCount(String insta) {
        Instagram4j instagram = Instagram4j.builder()
                .username(instaLogin)
                .password(instaPassword)
                .build();
        try {
            instagram.setup();
            instagram.login();
            InstagramSearchUsernameResult usernameResult = instagram.sendRequest(new InstagramSearchUsernameRequest(insta));
            int subsCount = usernameResult.getUser().getFollower_count();
            return String.valueOf(subsCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Неизвестно";
    }
}
