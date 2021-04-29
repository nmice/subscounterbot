package ru.neginskiy.subscounterbot.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Данные анкеты пользователя
 */

@Data
public class UserProfileData implements Serializable {
    private String name;
    private String gender;
    private String color;
    private String movie;
    private String song;
    private int age;
    private int number;


    @Override
    public String toString() {
        return String.format("Имя: %s%nВозраст: %d%nПол: %s%nЛюбимая цифра: %d%n" +
                        "Цвет: %s%nФильм: %s%nПесня: %s%n", getName(), getAge(), getGender(), getNumber(),
                getColor(), getMovie(), getSong());
    }
}
