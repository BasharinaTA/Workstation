package com.company;

import java.util.Scanner;

public class Specialist extends People implements IEntry {
    private PolyclinicManager polyclinicManager;

    public Specialist(PolyclinicManager polyclinicManager) {
        this.polyclinicManager = polyclinicManager;
    }

    public void listSpecialistMenu() {
        System.out.println("Работа со списком специалистов.");
    }

    @Override
    public void addEntry() {
        System.out.println("Добавить запись.");
    }

    @Override
    public void updateEntry() {
        System.out.println("Редактировать запись.");
    }

    @Override
    public void deleteEntry() {
        System.out.println("Удалить запись.");
    }

    public void specialistMenu() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Выберите одно из указанных действий, введя соответствующую цифру.\n" +
                "Работа со списком специалистов - 1.\n" +
                "Добавить нового специалиста - 2.\n" +
                "Редактировать запись специалиста - 3.\n" +
                "Удалить запись о специалисте - 4.\n" +
                "Если вы хотите вернуться на главную форму, введите 0.\n");
        int inputValue = sc.nextInt();
        if (inputValue == 0) {
            polyclinicManager.mainMenu();
        } else if (inputValue == 1) {
            listSpecialistMenu();
        } else if (inputValue == 2) {
            addEntry();
        } else if (inputValue == 3) {
            updateEntry();
        } else if (inputValue == 4) {
            deleteEntry();
        }
    }
}
