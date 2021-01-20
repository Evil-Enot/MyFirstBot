package com.example.MyFirstBot.bot;

import java.util.ArrayList;
import java.util.Random;

public class Questions {

    private String newQuestion;
    private String answer;
    private ArrayList<Integer> pool = new ArrayList<Integer>();

    public void getQuestion() {
        Random random = new Random();
        int number = random.nextInt(6);
        if (!pool.contains(number)) {
            pool.add(number);
            questions(number);
        } else if (pool.size() == 6) {
            pool.clear();
            getQuestion();
        } else {
            getQuestion();
        }
    }

    private void questions(int number) {
        switch (number) {
            case 0:
                newQuestion = "Вы заходите в тёмную кухню. В ней есть свеча, керосиновая лампа и газовая плита. Что вы зажжёте в первую очередь?";
                answer = "спичку";
                break;
            case 1:
                newQuestion = "Если пять кошек ловят пять мышей за пять минут, то сколько времени нужно одной кошке, чтобы поймать одну мышку? Напишите цифру:";
                answer = "5";
                break;
            case 2:
                newQuestion = "Полтора судака стоят полтора рубля. Сколько стоят 13 судаков? Напишите цифру:";
                answer = "13";
                break;
            case 3:
                newQuestion = "Как известно, все русские женские имена оканчиваются либо на букву «а», либо на букву «я»: " +
                        "Анна, Мария, Ирина, Наталья, Ольга и т.д. Однако есть одно-единственное женское имя, которое  оканчивается на другую букву. Назовите его.";
                answer = "любовь";
                break;
            case 4:
                newQuestion = "На столе лежат две монеты, в сумме они дают 3 рубля. Одна из них — не 1 рубль. Какие это монеты? Напишите цифры через пробел";
                answer = "1 2";
                break;
            case 5:
                newQuestion = "На каком языке говорят молча?";
                answer = "язык жестов";
                break;
        }
    }

    public String getNewQuestion() {
        return newQuestion;
    }

    public String getAnswer() {
        return answer;
    }
}
