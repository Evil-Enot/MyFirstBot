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

    private boolean waitingForAnAnswer = false;
    private String answer = "";
    private Questions questions = new Questions();

    @Override
    public void onUpdateReceived(Update update) {
        Message msg = update.getMessage();

        //Если в msg содержится текст, то выбираем что дальше делать
        if (msg != null && msg.hasText()) {
            String text = msg.getText();

            if (prevCommand.isEmpty() && !waitingForAnAnswer) {
                switch (text) {
                    case "/start":
                        createKeyboard();
                        sendMsg(msg, "Hello! This is my first bot. \nChoose from menu:");
                        break;
                    case "Divide into paragraphs":
                        sendMsg(msg, "Write your text:");
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
                    case "Get riddle":
                        waitingForAnAnswer = true;
                        questions.getQuestion();
                        answer = questions.getAnswer();
                        sendMsg(msg, questions.getNewQuestion());
                        break;
                    case "Stop riddle":
                        waitingForAnAnswer = false;
                        sendMsg(msg, "You didn't get riddle");
                        break;
                    default:
                        sendMsg(msg, "You write: " + text);
                        break;
                }
            } else {
                // Ветка, если пользователь хочет модифицировать текст
                if (!prevCommand.isEmpty()) {
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
                // Если отгадывает загадку
                else {
                    if (text.equals("Stop riddle")) {
                        waitingForAnAnswer = false;
                        sendMsg(msg, "You stop riddle");
                    } else if (text.equals("Get riddle")) {
                        sendMsg(msg, "You already have a riddle");
                    } else {
                        if (answer.equals(text.trim().toLowerCase())) {
                            sendMsg(msg, "You're right!!! Congratulations!!!");
                            waitingForAnAnswer = false;
                        } else sendMsg(msg, "Not correct, try again :с");
                    }
                }
            }
        }
    }

    //метод для создания пользовательского меню
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

        // Третья строчка клавиатуры
        KeyboardRow keyboardFirstRow3 = new KeyboardRow();
        // Добавляем кнопки в третью строчку клавиатуры
        keyboardFirstRow3.add("Get riddle");
        keyboardFirstRow3.add("Stop riddle");

        // Добавляем все строчки клавиатуры в список
        keyboard.add(keyboardFirstRow1);
        keyboard.add(keyboardFirstRow2);
        keyboard.add(keyboardFirstRow3);

        // Устанваливаем этот список нашей клавиатуре
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    //Метод для вывода информации о боте
    private void aboutBot() {
        printMsg(newMsg, "This bot was created in order to make it easier for users to format text.\n" +
                "This bot can:\n" +
                "Divide entered text into paragraphs; \n" +
                "Apply different effects to text, such as italic, bold, etc.");
    }

    //метод для преобразования текста в полужирный
    private void boldText(String text) {
        printMsg(newMsg, "*" + text + "*");
    }

    //метод для преобразования текста в курсивный
    private void italicText(String text) {
        printMsg(newMsg, "_" + text + "_");
    }

    //метод для преобразования текста с абзацами
    private void divideTextIntoParagraphs(String text) {
        StringBuilder newText = new StringBuilder();
        newText.append(text).insert(0, "Your message with paragraphs: \n");
        printMsg(newMsg, newText.toString().replace("\n", "\r\n     "));
    }

    //метод для определения ChatId
    private void sendMsg(Message msg, String text) {
        newMsg.setChatId(msg.getChatId());
        printMsg(newMsg, text);
    }

    //метод для отправки сообщения
    private void printMsg(SendMessage msg, String text) {
        msg.enableMarkdown(true);
        msg.setText(text);
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
