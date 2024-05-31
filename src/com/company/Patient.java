package com.company;

import java.util.Objects;
import java.util.Scanner;
import java.util.List;
import java.util.Arrays;

public class Patient extends Person implements Entity<Patient> {
    private String insurancePolicy;

    public Patient() {
    }

    public Patient(String[] arr) {
        super(arr[0], arr[1], arr[2], arr[3]);
        this.insurancePolicy = arr[4];
    }

    public String getInsurancePolicy() {
        return insurancePolicy;
    }

    public void setInsurancePolicy(String insurancePolicy) {
        this.insurancePolicy = insurancePolicy;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Patient patient = (Patient) obj;
        return super.equals(patient) && insurancePolicy.equals(patient.getInsurancePolicy());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), insurancePolicy);
    }

    public int compareTo(Patient patient) {
        if (super.compareTo(patient) != 0) {
            return super.compareTo(patient);
        }
        if (!insurancePolicy.equals(patient.getInsurancePolicy())) {
            return insurancePolicy.compareTo(patient.getInsurancePolicy());
        }
        return 0;
    }

    @Override
    public String toString() {
        return super.toString() + " " + insurancePolicy + "\n";
    }

    public static void showMenu(Manager manager) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println(
                    "Выберите одно из указанных действий, введя соответствующее значение:\n" +
                            "1 - показать список пациентов\n" +
                            "2 - добавить нового пациента\n" +
                            "3 - редактировать запись пациента\n" +
                            "4 - удалить запись о пациенте\n" +
                            "0 - вернуться в главное меню");
            switch (sc.nextLine()) {
                case "0" -> {
                    return;
                }
                case "1" -> manager.showAllPatients();
                case "2" -> manager.addPatient();
                case "3" -> manager.updatePatient();
                case "4" -> manager.deletePatient();
                default -> System.out.println("Введено некорректное значение");
            }
        }
    }

    @Override
    public void init() {
        List<Answer> info = Arrays.asList(
                new Answer("Введите фамилию пациента", this::setLastname),
                new Answer("Введите имя пациента", this::setFirstname),
                new Answer("Введите дату рождения пациента", this::setDateOfBirth),
                new Answer("Введите номер полиса пациента",  this::setInsurancePolicy));
        personFill(info);
    }

    @Override
    public void update() {
        List<Answer> info = Arrays.asList(
                new Answer("Введите фамилию пациента. Если изменение не требуется, нажмите \"Enter\"", this::setLastname),
                new Answer("Введите имя пациента. Если изменение не требуется, нажмите \"Enter\"", this::setFirstname),
                new Answer("Введите дату рождения пациента. Если изменение не требуется, нажмите \"Enter\"", this::setDateOfBirth),
                new Answer("Введите номер полиса пациента. Если изменение не требуется, нажмите \"Enter\"", this::setInsurancePolicy)
        );
    }
}
