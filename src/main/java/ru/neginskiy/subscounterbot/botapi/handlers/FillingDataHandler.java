package ru.neginskiy.subscounterbot.botapi.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.neginskiy.subscounterbot.botapi.BotState;
import ru.neginskiy.subscounterbot.botapi.InputMessageHandler;
import ru.neginskiy.subscounterbot.cache.DataCache;
import ru.neginskiy.subscounterbot.model.UserData;
import ru.neginskiy.subscounterbot.service.ButtonsProvider;
import ru.neginskiy.subscounterbot.service.ReplyMessagesService;
import ru.neginskiy.subscounterbot.service.StatService;
import ru.neginskiy.subscounterbot.service.UsersProfileDataService;
import ru.neginskiy.subscounterbot.utils.Emojis;

/**
 * Обработчик состояния заполнения данных FILLING_PROFILE
 */
@Slf4j
@Component
public class FillingDataHandler implements InputMessageHandler {
    private final DataCache userDataCache;
    private final ReplyMessagesService messagesService;
    private final StatService statService;
    private final UsersProfileDataService profileDataService;
    private final ButtonsProvider buttonsProvider;

    public FillingDataHandler(DataCache userDataCache, ReplyMessagesService messagesService, StatService statService,
                              UsersProfileDataService profileDataService, ButtonsProvider buttonsProvider) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
        this.statService = statService;
        this.profileDataService = profileDataService;
        this.buttonsProvider = buttonsProvider;
    }

    @Override
    public SendMessage handle(Message message) {
        if (userDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.FILLING_PROFILE)) {
            userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.ASK_INSTA);
        }
        return processUsersInput(message);
    }

    @Override
    public BotApiMethod<?> processCallBack(CallbackQuery buttonQuery) {
        int userId = buttonQuery.getFrom().getId();
        UserData profileData = userDataCache.getUserProfileData(userId);
        long chatId = buttonQuery.getMessage().getChatId();
        BotApiMethod<?> callBackAnswer = null;

        // Обработка ответа по кнопкам на экране Insta
        if (buttonQuery.getData().equals("InstaYes")) {
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_INSTA_LOGIN);
            callBackAnswer = messagesService.getReplyMessageFromLocale(chatId, "reply.askInstaLogin");
        } else if (buttonQuery.getData().equals("InstaNo")) {
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_TWITTER);
            SendMessage replyToUser = messagesService.getReplyMessageFromLocale(chatId, "reply.askTwitter");
            replyToUser.setReplyMarkup(buttonsProvider.getYesNoButtonsMarkup("TwitterYes", "TwitterNo"));
            callBackAnswer = replyToUser;
        }

        // Обработка ответа по кнопкам на экране Twitter
        else if (buttonQuery.getData().equals("TwitterYes")) {
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_TWITTER_LOGIN);
            callBackAnswer = messagesService.getReplyMessageFromLocale(chatId, "reply.askTwitterLogin");
        } else if (buttonQuery.getData().equals("TwitterNo")) {
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_YOUTUBE);
            SendMessage replyToUser = messagesService.getReplyMessageFromLocale(chatId, "reply.askYouTube");
            replyToUser.setReplyMarkup(buttonsProvider.getYesNoButtonsMarkup("YouTubeYes", "YouTubeNo"));
            callBackAnswer = replyToUser;
        }

        // Обработка ответа по кнопкам на экране YouTube
        else if (buttonQuery.getData().equals("YouTubeYes")) {
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_YOUTUBE_LOGIN);
            callBackAnswer = messagesService.getReplyMessageFromLocale(chatId, "reply.askYouTubeLogin");
        } else if (buttonQuery.getData().equals("YouTubeNo")) {
            userDataCache.setUsersCurrentBotState(userId, BotState.PROFILE_FILLED);
            callBackAnswer = getResultMessage(profileData, chatId);
        } else {
            userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
        }

        return callBackAnswer;
    }

    private SendMessage getResultMessage(UserData profileData, long chatId) {
        String profileFilledMessage = messagesService.getReplyText("reply.profileFilled", Emojis.SPARKLES);
        String statMessage = statService.getStatistic(profileData);
        SendMessage replyToUser = new SendMessage(chatId, String.format("%s%n%n%s", profileFilledMessage, statMessage));
        replyToUser.setParseMode("HTML");
        return replyToUser;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.FILLING_PROFILE;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        String usersAnswer = inputMsg.getText();
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();

        UserData profileData = userDataCache.getUserProfileData(userId);
        BotState botState = userDataCache.getUsersCurrentBotState(userId);
        SendMessage replyToUser = null;

        if (botState.equals(BotState.ASK_INSTA)) {
            replyToUser = messagesService.getReplyMessageFromLocale(chatId, "reply.askInsta");
            replyToUser.setReplyMarkup(buttonsProvider.getYesNoButtonsMarkup("InstaYes", "InstaNo"));
        }

        if (botState.equals(BotState.ASK_INSTA_LOGIN)) {
            profileData.setInsta(usersAnswer);
            replyToUser = messagesService.getReplyMessageFromLocale(chatId, "reply.askTwitter");
            replyToUser.setReplyMarkup(buttonsProvider.getYesNoButtonsMarkup("TwitterYes", "TwitterNo"));
        }

        if (botState.equals(BotState.ASK_TWITTER)) {
            replyToUser = messagesService.getReplyMessageFromLocale(chatId, "reply.askTwitterLogin");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_YOUTUBE);
        }

        if (botState.equals(BotState.ASK_TWITTER_LOGIN)) {
            profileData.setTwitter(usersAnswer);
            replyToUser = messagesService.getReplyMessageFromLocale(chatId, "reply.askYouTube");
            replyToUser.setReplyMarkup(buttonsProvider.getYesNoButtonsMarkup("YouTubeYes", "YouTubeNo"));
        }

        if (botState.equals(BotState.ASK_YOUTUBE)) {
            replyToUser = messagesService.getReplyMessageFromLocale(chatId, "reply.askYouTubeLogin");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_YOUTUBE_LOGIN);
        }

        if (botState.equals(BotState.ASK_YOUTUBE_LOGIN)) {
            userDataCache.setUsersCurrentBotState(userId, BotState.PROFILE_FILLED);
            profileData.setYouTube(usersAnswer);
            profileData.setChatId(chatId);
            profileDataService.saveUserProfileData(profileData);
            replyToUser = getResultMessage(profileData, chatId);
        }

        userDataCache.saveUserProfileData(userId, profileData);

        return replyToUser;
    }
}

