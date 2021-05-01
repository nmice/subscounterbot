package ru.neginskiy.subscounterbot.service;

import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.neginskiy.subscounterbot.model.UserProfileData;
import ru.neginskiy.subscounterbot.utils.Emojis;

/**
 * Сервис генерирует статистику на основе логинов соцсетей
 */
@Service
public class StatService {
    private ReplyMessagesService messagesService;
    @Value("${social.instaLogin}")
    private String instaLogin;
    @Value("${social.instaPassword}")
    private String instaPassword;


    public StatService(ReplyMessagesService messagesService) {
        this.messagesService = messagesService;
    }

    public String getStatistic(UserProfileData profileData) {
        String instaSubs = getInstaSubsCount(profileData.getInsta());
        String twitterSubs = getTwitterSubsCount(profileData.getTwitter());
        String youtubeSubs = getYouTubeSubsCount(profileData.getYouTube());

        String replyMessagePropertie = String.format(
                "%s %s: %s\r\n%s %s: %s\r\n%s %s: %s\r\n",
                Emojis.STAR, "INSTAGRAM", instaSubs,
                Emojis.STAR, "TWITTER", twitterSubs,
                Emojis.STAR, "YOUTUBE", youtubeSubs
        );
        return replyMessagePropertie;
    }

    private String getYouTubeSubsCount(String youTube) {
        return "Не реализовано";
    }

    private String getTwitterSubsCount(String twitter) {
        return "Не реализовано";
    }

    private String getInstaSubsCount(String insta) {
        Instagram4j instagram = Instagram4j.builder()
                .username(instaLogin)
                .password(instaPassword)
                .build();
        try {
            instagram.setup();
            instagram.login();
            InstagramSearchUsernameResult usernameResult = instagram.sendRequest(new InstagramSearchUsernameRequest(insta));
            int subsCount = usernameResult.getUser().getFollower_count();
            int subsLevel = String.valueOf(usernameResult.getUser().getFollower_count()).length();
            String comment;
            if (subsLevel <= 5) {
                comment = messagesService.getReplyText("reply.level" + subsLevel);
            } else {
                comment = messagesService.getReplyText("reply.level" + 6);
            }
            return String.format("%d <i>%s</i>", subsCount, comment);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Неизвестно";
    }
}
