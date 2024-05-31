package com.company;

import java.util.Scanner;

public class Authentication {
    private String login;
    private String password;

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Manager verificationLogin() {
        Scanner sc = new Scanner(System.in);
        do {
            System.out.println("Введите логин");
            this.setLogin(sc.nextLine());
            if (isCheckLogin()) {
                this.verificationPassword();
                return new Manager();
            } else {
                System.out.println("Логин введён неверно. Попробуйте ещё раз.");
            }
        }
        while (!isCheckLogin());
        return null;
    }

    private boolean isCheckLogin() {
        return this.getLogin().equals("111"); // TODO Реализовать работу с файлом.
    }

    private void verificationPassword() {
        Scanner sc = new Scanner(System.in);
        do {
            System.out.println("Введите пароль");
            this.setPassword(sc.nextLine());
            if (isCheckPassword()) {
              return;
            } else {
                System.out.println("Пароль введён неверно. Попробуйте ещё раз");
            }
        }
        while (!isCheckPassword());
    }

    private boolean isCheckPassword() {
        return this.getPassword().equals("111"); // TODO Реализовать работу с файлом.
    }
}
