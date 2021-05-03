package ru.neginskiy.subscounterbot.service;


import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.neginskiy.subscounterbot.dto.TwitterResponseDto;

@Service
public class TwitterService {

    public String getTwitterSubsCount(String twitter) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            TwitterResponseDto[] stringPosts = restTemplate.getForObject(
                    String.format("https://cdn.syndication.twimg.com/widgets/followbutton/info.json?screen_names=%s", twitter),
                    TwitterResponseDto[].class);
            return String.valueOf(stringPosts[0].getFollowers_count());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Неизвестно";
    }
}
