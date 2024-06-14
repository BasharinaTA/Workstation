package com.company.clinic;

import com.company.exceptions.InvalidInputException;
import com.company.utils.Survey;

import java.util.List;
import java.util.Scanner;

public abstract class Entity {

    abstract void init(Scanner sc);

    abstract void update(Scanner sc);

    abstract boolean check(List<Entity> entities);

    protected void fillEntity(Scanner sc, List<Survey> list) {
        String str;
        for (Survey survey : list) {
            System.out.println(survey.getQuestion());
            str = sc.nextLine();
            while (true) {
                try {
                    survey.getConsumer().accept(str);
                    break;
                } catch (InvalidInputException e) {
                    System.out.println(e.getMessage() + "\n" + survey.getQuestion());
                    str = sc.nextLine();
                }
            }
        }
    }

    protected void updateEntity(Scanner sc, List<Survey> list) {
        String str;
        for (Survey survey : list) {
            System.out.println(survey.getQuestion());
            str = sc.nextLine();
            while (!str.isEmpty()) {
                try {
                    survey.getConsumer().accept(str);
                    break;
                } catch (InvalidInputException e) {
                    System.out.println(e.getMessage() + "\n" + survey.getQuestion());
                    str = sc.nextLine();
                }
            }
        }
    }
}
