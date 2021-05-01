package ru.neginskiy.subscounterbot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import ru.neginskiy.subscounterbot.SubsCounterBot;
import ru.neginskiy.subscounterbot.botapi.TelegramFacade;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "bot")
public class BotConfig {
    private String webHookPath;
    private String botUserName;
    private String botToken;

    @Bean
    public SubsCounterBot sourceCounterBot(TelegramFacade telegramFacade) {
        DefaultBotOptions options = ApiContext
                .getInstance(DefaultBotOptions.class);

        SubsCounterBot sourceCounterBot = new SubsCounterBot(options, telegramFacade);
        sourceCounterBot.setBotUserName(botUserName);
        sourceCounterBot.setBotToken(botToken);
        sourceCounterBot.setWebHookPath(webHookPath);

        return sourceCounterBot;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();

        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}