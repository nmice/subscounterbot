package ru.neginskiy.subscounterbot.model;

import lombok.Data;
import ru.neginskiy.subscounterbot.enums.SocialMediaType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Данные пользователя
 */
@Data
public class UserData implements Serializable {
    private String id = new Random().toString();
    private Map<SocialMediaType, String> accNameBySmTypeMap = new HashMap<>();
    private String twitter;
    private String youTube;

    private long chatId;

    @Override
    public String toString() {
        return String.format("Instagram: %s%nTwitter: %s%nYouTube: %s%n",
                accNameBySmTypeMap.get(SocialMediaType.INSTAGRAM),
                accNameBySmTypeMap.get(SocialMediaType.TWITTER),
                accNameBySmTypeMap.get(SocialMediaType.YOUTUBE));
    }
}
