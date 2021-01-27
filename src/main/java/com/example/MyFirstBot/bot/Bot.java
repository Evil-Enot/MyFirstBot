package com.example.MyFirstBot.bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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
    private Keyboard keyboard = new Keyboard();

    @Override
    public void onUpdateReceived(Update update) {
        Message msg = update.getMessage();

        //Если в msg содержится текст, то выбираем что дальше делать
        if (msg != null && msg.hasText()) {
            String text = msg.getText();

            if (prevCommand.isEmpty() && !waitingForAnAnswer) {
                switch (text) {
                    case "/start":
                        keyboard.createKeyboard(newMsg, 0);
                        sendMsg(msg, "Hello! This is my first bot. \nChoose from menu:");
                        break;
                    case "Text":
                        keyboard.createKeyboard(newMsg, 1);
                        sendMsg(msg, "Choose from the menu:");
                        break;
                    case "Riddles":
                        keyboard.createKeyboard(newMsg, 2);
                        sendMsg(msg, "Good luck");
                        break;
                    case "About":
                        aboutBot();
                        break;
                    case "Return to main menu":
                        keyboard.createKeyboard(newMsg, 0);
                        sendMsg(msg, "You have returned to the main menu:");
                        break;
                    case "Divide text":
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
                    case "Get riddle":
                        waitingForAnAnswer = true;
                        questions.getQuestion();
                        answer = questions.getAnswer();
                        sendMsg(msg, questions.getNewQuestion());
                        break;
                    case "Stop":
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
                    switch (prevCommand) {
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
                    if (text.equals("Stop")) {
                        waitingForAnAnswer = false;
                        sendMsg(msg, "You stop riddle");
                        sendMsg(msg, "Answer: " + answer);
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
