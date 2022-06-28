package com.company;

import java.util.Scanner;

public class Appointment {
    private Patient patient;
    private Specialist specialist;
    private final PolyclinicManager polyclinicManager;

    public Appointment(PolyclinicManager polyclinicManager) {
        this.polyclinicManager = polyclinicManager;
    }

    public void createAppointment() {
        System.out.println("Добавить запись.");
    }

    public void updateAppointment() {
        System.out.println("Редактировать запись.");
    }

    public void deleteAppointment() {
        System.out.println("Удалить запись.");
    }

    public void appointmentMenu() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Выберите одно из указанных действий, введя соответствующую цифру.\n" +
                "Добавить запись - 1.\n" +
                "Редактировать запись - 2.\n" +
                "Отменить запись - 3.\n" +
                "Если вы хотите вернуться на главную форму, введите 0\n");
        int inputValue = sc.nextInt();
        if (inputValue == 0) {
            polyclinicManager.mainMenu();
        } else if (inputValue == 1) {
            createAppointment();
        } else if (inputValue == 2) {
            updateAppointment();
        } else if (inputValue == 3) {
            deleteAppointment();
        }
    }
}
