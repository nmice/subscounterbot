package ru.neginskiy.subscounterbot.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * Данные анкеты пользователя
 */

@Data
@Document(collection = "userProfileData")
public class UserProfileData implements Serializable {
    @Id
    private String id;
    private String name;
    private String gender;
    private String color;
    private String movie;
    private String song;
    private int age;
    private int number;
    private long chatId;


    @Override
    public String toString() {
        return String.format("Имя: %s%nВозраст: %d%nПол: %s%nЛюбимая цифра: %d%n" +
                        "Цвет: %s%nФильм: %s%nПесня: %s%n", getName(), getAge(), getGender(), getNumber(),
                getColor(), getMovie(), getSong());
    }
}
