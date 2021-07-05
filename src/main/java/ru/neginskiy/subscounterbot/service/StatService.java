package ru.neginskiy.subscounterbot.service;

import org.springframework.stereotype.Service;
import ru.neginskiy.subscounterbot.enums.SocialMediaType;
import ru.neginskiy.subscounterbot.model.UserData;
import ru.neginskiy.subscounterbot.service.socialmedia.InstagramService;
import ru.neginskiy.subscounterbot.service.socialmedia.SocialMediaService;
import ru.neginskiy.subscounterbot.service.socialmedia.TwitterService;
import ru.neginskiy.subscounterbot.service.socialmedia.YouTubeService;
import ru.neginskiy.subscounterbot.utils.Emojis;

/**
 * Сервис генерирует статистику на основе логинов соцсетей
 */
@Service
public class StatService {
    private final CommentService commentService;
    private final InstagramService instagramService;
    private final YouTubeService youTubeService;
    private final TwitterService twitterService;

    public StatService(CommentService commentService, InstagramService instagramService, YouTubeService youTubeService,
                       TwitterService twitterService) {
        this.commentService = commentService;
        this.instagramService = instagramService;
        this.youTubeService = youTubeService;
        this.twitterService = twitterService;
    }

    public String getStatistic(UserData userData) {
        String instagramInfo = getSubsWithComment(instagramService, SocialMediaType.INSTAGRAM, userData);
        String twitterInfo = getSubsWithComment(twitterService, SocialMediaType.TWITTER, userData);
        String youTubeInfo = getSubsWithComment(youTubeService, SocialMediaType.YOUTUBE, userData);
        return String.format(
                "%s %s: %s\r\n%s %s: %s\r\n%s %s: %s\r\n",
                Emojis.STAR, "INSTAGRAM", instagramInfo,
                Emojis.STAR, "TWITTER", twitterInfo,
                Emojis.STAR, "YOUTUBE", youTubeInfo
        );
    }

    private String getSubsWithComment(SocialMediaService service, SocialMediaType type, UserData userData) {
        String subs = service.getSubsCount(userData.getAccNameBySmTypeMap().get(type));
        String comment = commentService.getCommentBySubsCount(subs);
        return String.format("%s <i>%s</i>", subs, comment);
    }
}
