package ru.neginskiy.subscounterbot.utils;

import com.vdurmont.emoji.EmojiParser;
import lombok.AllArgsConstructor;

/**
 * Перечесление с используемыми смайликами
 */
@AllArgsConstructor
public enum Emojis {
    SPARKLES(EmojiParser.parseToUnicode(":sparkles:")),
    SCROLL(EmojiParser.parseToUnicode(":scroll:")),
    MOYAI(EmojiParser.parseToUnicode(":moyai:")),
    STAR(EmojiParser.parseToUnicode(":star_struck:"));

    private String emojiName;

    @Override
    public String toString() {
        return emojiName;
    }
}