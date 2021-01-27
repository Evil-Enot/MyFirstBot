package com.example.MyFirstBot.bot;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

public class Questions {

    private String newQuestion;
    private String answer;
    private ArrayList<Integer> pool = new ArrayList<Integer>();
    private Properties prop = new Properties();

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
        try {
            FileInputStream fileInputStream = new FileInputStream("src/main/resources/questions.properties");
            prop.load(fileInputStream);
            newQuestion = prop.getProperty((number + 1) + ".question");
            answer = prop.getProperty((number + 1) + ".answer");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNewQuestion() {
        return newQuestion;
    }

    public String getAnswer() {
        return answer;
    }
}
