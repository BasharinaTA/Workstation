package com.company;

import java.util.Scanner;

public class PolyclinicManager extends People {
    private Appointment appointment = new Appointment(this);
    private Patient patient = new Patient(this);
    private Specialist specialist = new Specialist(this);

    public void mainMenu() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Выберите одно из указанных действий, введя соответствующую цифру. \n" +
                "Запись на прием - 1.\n" +
                "Работа с пациентом - 2.\n" +
                "Работа со специалистом - 3. \n");
        int inputValue = sc.nextInt();
        if (inputValue == 1) {
            appointment.appointmentMenu();
        } else if (inputValue == 2) {
            patient.patientMenu();
        } else if (inputValue == 3) {
            specialist.specialistMenu();
        }
    }
}
