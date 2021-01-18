package com.example.MyFirstBot.bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
public class Bot extends TelegramLongPollingBot {

    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String botToken;

    private SendMessage newMsg = new SendMessage();
    private String prevCommand = "";

    @Override
    public void onUpdateReceived(Update update) {
        Message msg = update.getMessage();

        //Если в msg содержится текст, то выбираем что дальше делать
        if (msg != null && msg.hasText()) {
            String text = msg.getText();
            if (prevCommand.isEmpty()) {
                switch (text) {
                    case "/start":
                        createKeyboard();
                        sendMsg(msg, "Hello! This is my first bot. \nChoose from menu:");
                        break;
                    case "Divide into paragraphs":
                        sendMsg(msg, "Write your text below, the first line of your message will be displayed unchanged: :");
                        prevCommand = "Divide";
                        break;
                    case "Italic":
                        sendMsg(msg, "Write your text:");
                        prevCommand = "Italic";
                        break;
                    case "Bold":
                        prevCommand = "Bold";
                        sendMsg(msg, "Write your text:");
                        break;
                    case "About bot":
                        aboutBot();
                        break;
                    default:
                        sendMsg(msg, "You write: " + text);
                        break;
                }
            } else {
                switch (prevCommand){
                    case "Divide":
                        divideTextIntoParagraphs(text);
                        break;
                    case "Italic":
                        italicText(text);
                        break;
                    case "Bold":
                        boldText(text);
                        break;
                }
                prevCommand = "";
            }
        }
    }

    private void createKeyboard() {
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
        // Добавляем кнопки в первую строчку клавиатуры
        keyboardFirstRow1.add("Divide into paragraphs");
        keyboardFirstRow1.add("Italic");

        // Вторая строчка клавиатуры
        KeyboardRow keyboardFirstRow2 = new KeyboardRow();
        // Добавляем кнопки во вторую строчку клавиатуры
        keyboardFirstRow2.add("Bold");
        keyboardFirstRow2.add("About bot");

        // Добавляем все строчки клавиатуры в список
        keyboard.add(keyboardFirstRow1);
        keyboard.add(keyboardFirstRow2);

        // Устанваливаем этот список нашей клавиатуре
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    //Метод для вывода информации о боте
    private void aboutBot() {
        printMsg(newMsg, "This bot was created in order to make it easier for users to format text.\n" +
                "This bot can:\n" +
                "Divide entered text into paragraphs, иut with some remark. The first line of your message, " +
                "due to the peculiarity of Telegram, will be displayed unchanged. \n" +
                "Apply different effects to text, such as italic, bold, etc.");
    }

    private void boldText(String text) {
        printMsg(newMsg, "*" + text + "*");
    }

    private void italicText(String text) {
        printMsg(newMsg, "_" + text + "_");
    }

    private void divideTextIntoParagraphs(String text) {
        printMsg(newMsg, text.replace("\n", "\r\n     "));
    }

    //метод для отправки сообщения ботом
    private void sendMsg(Message msg, String text) {
        newMsg.setChatId(msg.getChatId());
        printMsg(newMsg, text);
    }

    //метод для отправки сообщения
    private void printMsg(SendMessage msg, String text) {
        newMsg.enableMarkdown(true);
        newMsg.setText(text);
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
