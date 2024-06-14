package com.company.utils;

import java.util.function.Consumer;

public class Survey {

    private String question;
    private Consumer<String> consumer;

    public Survey(String question, Consumer<String> consumer) {
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
