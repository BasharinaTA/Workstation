package com.company;

import java.util.function.Consumer;

public class Answer {

    private String question;
    private Consumer<String> consumer;
    public Answer(String question, Consumer<String> consumer) {
        this.question = question;
        this.consumer = consumer;
    }

    public String getQuestion() {
        return question;
    }

    public Consumer<String> getConsumer() {
        return consumer;
    }
}
