package com.company;

public class Main {

    public static void main(String[] args) {
        Authentication aut = new Authentication();
        var user = aut.verificationLogin();
        user.mainMenu();
    }
}
