package ru.neginskiy.subscounterbot.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Random;

/**
 * Данные пользователя
 */
@Data
public class UserProfileData implements Serializable {
    private String id = new Random().toString();
    private String insta;
    private String twitter;
    private String youTube;
    private long chatId;

    @Override
    public String toString() {
        return String.format("Instagram: %s%nTwitter: %s%nYouTube: %s%n",
                getInsta(), getTwitter(), getYouTube());
    }
}
