package ru.neginskiy.subscounterbot.service.socialmedia;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.neginskiy.subscounterbot.dto.TwitterResponseDto;

/**
 * Сервис по работе с Twitter API через YouTube Data API (v3)
 */
@Service
public class TwitterService implements SocialMediaService {

    public String getSubsCount(String twitter) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            TwitterResponseDto[] stringPosts = restTemplate.getForObject(
                    String.format("https://cdn.syndication.twimg.com/widgets/followbutton/info.json?screen_names=%s", twitter),
                    TwitterResponseDto[].class);
            if (stringPosts != null && stringPosts.length > 0) {
                return String.valueOf(stringPosts[0].getFollowers_count());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Неизвестно";
    }
}
