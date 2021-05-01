package ru.neginskiy.subscounterbot.botapi.handlers.fillingprofile;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.neginskiy.subscounterbot.botapi.BotState;
import ru.neginskiy.subscounterbot.botapi.InputMessageHandler;
import ru.neginskiy.subscounterbot.cache.DataCache;
import ru.neginskiy.subscounterbot.model.UserProfileData;
import ru.neginskiy.subscounterbot.service.ReplyMessagesService;
import ru.neginskiy.subscounterbot.service.StatService;
import ru.neginskiy.subscounterbot.service.UsersProfileDataService;
import ru.neginskiy.subscounterbot.service.ButtonsProvider;
import ru.neginskiy.subscounterbot.utils.Emojis;

/**
 * Обработчик состояния заполнения данных FILLING_PROFILE
 */
@Slf4j
@Component
public class FillingProfileHandler implements InputMessageHandler {
    private DataCache userDataCache;
    private ReplyMessagesService messagesService;
    private StatService statService;
    private UsersProfileDataService profileDataService;
    private ButtonsProvider buttonsProvider;

    public FillingProfileHandler(DataCache userDataCache, ReplyMessagesService messagesService,
                                 StatService statService, UsersProfileDataService profileDataService,
                                 ButtonsProvider buttonsProvider) {
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
    public BotState getHandlerName() {
        return BotState.FILLING_PROFILE;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        String usersAnswer = inputMsg.getText();
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();

        UserProfileData profileData = userDataCache.getUserProfileData(userId);
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

            String profileFilledMessage = messagesService.getReplyText("reply.profileFilled", Emojis.SPARKLES);
            String statMessage = statService.getStatistic(profileData);
            replyToUser = new SendMessage(chatId, String.format("%s%n%n%s", profileFilledMessage, statMessage));
            replyToUser.setParseMode("HTML");
        }

        userDataCache.saveUserProfileData(userId, profileData);

        return replyToUser;
    }
}

