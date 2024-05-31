package com.company;

public class Main {

    public static void main(String[] args) {
        Authentication auth = new Authentication();
        Manager user = auth.verificationLogin();
        user.mainMenu();
    }
}
