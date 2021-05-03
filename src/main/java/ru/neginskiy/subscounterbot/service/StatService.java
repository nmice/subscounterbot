package ru.neginskiy.subscounterbot.service;

import org.springframework.stereotype.Service;
import ru.neginskiy.subscounterbot.model.UserData;
import ru.neginskiy.subscounterbot.utils.Emojis;

/**
 * Сервис генерирует статистику на основе логинов соцсетей
 */
@Service
public class StatService {
    private final CommentService commentService;
    private final InstaService instaService;
    private final YouTubeService youTubeService;

    public StatService(CommentService commentService,
                       InstaService instaService,
                       YouTubeService youTubeService) {
        this.commentService = commentService;
        this.instaService = instaService;
        this.youTubeService = youTubeService;
    }

    public String getStatistic(UserData profileData) {
        String instaSubs = instaService.getInstaSubsCount(profileData.getInsta());
        String instaComment = commentService.getCommentBySubsCount(instaSubs);
        String twitterSubs = getTwitterSubsCount(profileData.getTwitter());
        String youtubeSubs = youTubeService.getYouTubeSubsCount(profileData.getYouTube());
        String youTubeComment = commentService.getCommentBySubsCount(youtubeSubs);

        String replyMessagePropertie = String.format(
                "%s %s: %s <i>%s</i>\r\n%s %s: %s\r\n%s %s: %s <i>%s</i>\r\n",
                Emojis.STAR, "INSTAGRAM", instaSubs, instaComment,
                Emojis.STAR, "TWITTER", twitterSubs,
                Emojis.STAR, "YOUTUBE", youtubeSubs, youTubeComment
        );
        return replyMessagePropertie;
    }

    private String getTwitterSubsCount(String twitter) {
        return "Не реализовано";
    }


}
