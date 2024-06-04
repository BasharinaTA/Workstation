package com.company;

import java.util.Scanner;

public class Appointment extends Entity {
    private Patient patient;
    private Doctor doctor;

    @Override
    public void init() {
        System.out.println("Добавить запись.");
    }

    @Override
    public void update() {
        System.out.println("Редактировать запись.");
    }





    public static void showMenu(Manager manager) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println(
                    "Выберите одно из указанных действий, введя соответствующее значение:\n" +
                            "1 - добавить запись\n" +
                            "2 - редактировать запись\n" +
                            "3 - отменить запись\n" +
                            "0 - вернуться в главное меню");
            switch (sc.nextLine()) {
                case "0" -> {
                    return;
                }
                case "1" -> manager.addAppointment();
                case "2" -> manager.updateAppointment();
                case "3" -> manager.deleteAppointment();
                default -> System.out.println("Введено некорректное значение");
            }
        }
    }
}
