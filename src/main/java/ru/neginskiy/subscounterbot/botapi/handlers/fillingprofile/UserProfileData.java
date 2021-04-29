package ru.neginskiy.subscounterbot.botapi.handlers.fillingprofile;

import lombok.Data;

/**
 * Данные анкеты пользователя
 */

@Data
public class UserProfileData {
    private String name;
    private String gender;
    private String color;
    private String movie;
    private String song;
    private int age;
    private int number;
}
