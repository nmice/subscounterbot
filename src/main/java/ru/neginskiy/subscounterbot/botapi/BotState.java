package ru.neginskiy.subscounterbot.botapi;

public enum BotState {
    ASK_WILL_WE_WORK,
    ASK_SOCIAL_MEDIA,
    ASK_INSTA_LOGIN,
    ASK_TWITTER_LOGIN,
    ASK_YOUTUBE_LOGIN,
    ASK_NUMBER,
    ASK_MOVIE,
    ASK_SONG,
    FILLING_PROFILE,
    PROFILE_FILLED,
    SHOW_USER_PROFILE,
    SHOW_MAIN_MENU,//Показать меню - Узнать статистику по аккаунту
    SHOW_HELP_MENU
}
