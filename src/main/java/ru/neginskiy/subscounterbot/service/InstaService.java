package ru.neginskiy.subscounterbot.service;


import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class InstaService {
    private final String instaLogin;
    private final String instaPassword;

    public InstaService(@Value("${social.instaLogin}") String instaLogin,
                        @Value("${social.instaPassword}") String instaPassword) {
        this.instaLogin = instaLogin;
        this.instaPassword = instaPassword;
    }

    public String getInstaSubsCount(String insta) {
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
