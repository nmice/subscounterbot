package ru.neginskiy.subscounterbot.botapi;

/**
 * Состояния бота для подбора реакции или ответа, также используется как имена обработчика
 */
public enum BotState {
    ASK_READY,
    ASK_INSTA,
    ASK_INSTA_LOGIN,
    ASK_TWITTER,
    ASK_TWITTER_LOGIN,
    ASK_YOUTUBE,
    ASK_YOUTUBE_LOGIN,
    FILLING_PROFILE,
    PROFILE_FILLED,
    SHOW_USER_SOCIAL_MEDIA_STATS,
    SHOW_MAIN_MENU,
    SHOW_HELP_MENU
}
