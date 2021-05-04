package ru.neginskiy.subscounterbot.service;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Сервис отображения кнопок с вариантами ответов
 */
@Component
public class ButtonsProvider {

    public InlineKeyboardMarkup getYesNoButtonsMarkup(String asYes, String asNo) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton buttonYes = new InlineKeyboardButton().setText("Да");
        InlineKeyboardButton buttonNo = new InlineKeyboardButton().setText("Нет");

        //У всех кнопок должно быть задано callBackData, иначе будет ошибка !
        buttonYes.setCallbackData(asYes);
        buttonNo.setCallbackData(asNo);

        List<InlineKeyboardButton> keyboardButtonsRow1 = Arrays.asList(buttonYes, buttonNo);
        List<List<InlineKeyboardButton>> rowList = Collections.singletonList(keyboardButtonsRow1);

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
}