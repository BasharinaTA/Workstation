package com.company;

import com.company.exception.InvalidInputException;

import java.util.List;
import java.util.Scanner;

public abstract class Entity {
    abstract void init();
    abstract void update();

    protected void fillEntity(List<Answer> info) {
        Scanner sc = new Scanner(System.in);
        for (Answer answer : info) {
            System.out.println(answer.getQuestion());
            String str = sc.nextLine();
            while (true) {
                try {
                    answer.getConsumer().accept(str);
                    break;
                } catch (InvalidInputException e) {
                    System.out.println(e.getMessage() + "\n" + answer.getQuestion());
                    str = sc.nextLine();
                }
            }
        }
    }

    protected void updateEntity(List<Answer> info) {
        Scanner sc = new Scanner(System.in);
        for (Answer answer : info) {
            System.out.println(answer.getQuestion());
            String str = sc.nextLine();
            while (!str.isEmpty()) {
                try {
                    answer.getConsumer().accept(str);
                    break;
                } catch (InvalidInputException e) {
                    System.out.println(e.getMessage() + "\n" + answer.getQuestion());
                    str = sc.nextLine();
                }
            }
        }
    }
}
