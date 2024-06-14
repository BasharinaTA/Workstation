package com.company.utils;

import java.util.function.Function;

public class Search<T> {

    private String question;
    private Function<T, String> function;

    public Search(String question, Function<T, String> function) {
        this.question = question;
        this.function = function;
    }

    public String getQuestion() {
        return question;
    }

    public Function<T, String> getFunction() {
        return function;
    }
}
