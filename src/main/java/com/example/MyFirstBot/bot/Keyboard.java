package com.example.MyFirstBot.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

class Keyboard {

    //метод для создания пользовательского меню
    void createKeyboard(SendMessage newMsg, int number) {
        newMsg.enableMarkdown(true);

        // Создаем клавиуатуру
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        newMsg.setReplyMarkup(replyKeyboardMarkup);

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>();

        // Первая строчка клавиатуры
        KeyboardRow keyboardFirstRow1 = new KeyboardRow();
        // Вторая строчка клавиатуры
        KeyboardRow keyboardFirstRow2 = new KeyboardRow();

        if (number == 0) {
            // Добавляем кнопки в первую строчку клавиатуры
            keyboardFirstRow1.add("Text");
            keyboardFirstRow1.add("Riddles");

            // Добавляем кнопки во вторую строчку клавиатуры
            keyboardFirstRow2.add("About");

            // Добавляем все строчки клавиатуры в список
            keyboard.add(keyboardFirstRow1);
            keyboard.add(keyboardFirstRow2);

            // Устанваливаем этот список нашей клавиатуре
            replyKeyboardMarkup.setKeyboard(keyboard);
        } else if (number == 1) {
            // Добавляем кнопки в первую строчку клавиатуры
            keyboardFirstRow1.add("Bold");
            keyboardFirstRow1.add("Italic");

            // Добавляем кнопки во вторую строчку клавиатуры
            keyboardFirstRow2.add("Divide text");
            keyboardFirstRow2.add("Return to main menu");

            // Добавляем все строчки клавиатуры в список
            keyboard.add(keyboardFirstRow1);
            keyboard.add(keyboardFirstRow2);

            // Устанваливаем этот список нашей клавиатуре
            replyKeyboardMarkup.setKeyboard(keyboard);
        } else if (number == 2) {
            // Добавляем кнопки в первую строчку клавиатуры
            keyboardFirstRow1.add("Get riddle");
            keyboardFirstRow1.add("Stop");

            // Добавляем кнопки во вторую строчку клавиатуры
            keyboardFirstRow2.add("Return to main menu");

            // Добавляем все строчки клавиатуры в список
            keyboard.add(keyboardFirstRow1);
            keyboard.add(keyboardFirstRow2);

            // Устанваливаем этот список нашей клавиатуре
            replyKeyboardMarkup.setKeyboard(keyboard);
        }
    }
}
