package com.company;

import com.company.auth.Authentication;
import com.company.clinic.Manager;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            Authentication auth = new Authentication();
            Manager user = auth.authenticate(sc);
            user.mainMenu(sc);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
