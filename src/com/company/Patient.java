package com.company;

import java.util.Scanner;

public class Patient extends People implements IEntry {
    private final PolyclinicManager polyclinicManager;

    public Patient(PolyclinicManager polyclinicManager) {
        this.polyclinicManager = polyclinicManager;
    }

    public void listPatientsMenu() {
        System.out.println("Вывод списка пациентов");
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

    /*
    public void addPatient() {
        System.out.println("Добавиь нового пациента");
    }

    public void updatePatient() {
        System.out.println("Редактировать запись пациента");
    }

    public void deletePatient() {
        System.out.println("Удалить запись пациента");
    }
     */

    public void patientMenu() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Выберите одно из указанных действий, введя соответствующую цифру.\n" +
                "Работа со списком пациентов - 1.\n" +
                "Добавить нового пациента - 2.\n" +
                "Редактировать запись пациента - 3.\n" +
                "Удалить запись о пациенте - 4.\n" +
                "Если вы хотите вернуться на главную форму, введите 0.\n");
        int inputValue = sc.nextInt();
        if (inputValue == 0) {
            polyclinicManager.mainMenu();
        } else if (inputValue == 1) {
            listPatientsMenu();
        } else if (inputValue == 2) {
            addEntry();
        } else if (inputValue == 3) {
            updateEntry();
        } else if (inputValue == 4) {
            deleteEntry();
        }
    }


}
