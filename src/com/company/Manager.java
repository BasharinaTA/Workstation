package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Manager {
    private static final Object lock = new Object();

    public void mainMenu() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println(
                    "Выберите одно из указанных действий, введя соответствующее значение: \n" +
                            "1 - запись на прием\n" +
                            "2 - работа с пациентом\n" +
                            "3 - работа со специалистом\n" +
                            "выход - выход из системы");
            switch (sc.nextLine()) {
                case "1" -> Appointment.showMenu(this);
                case "2" -> Patient.showMenu(this);
                case "3" -> Doctor.showMenu(this);
                case "выход" -> {
                    return;
                }
                default -> System.out.println("Введено некорректное значение");
            }
        }
    }

    public void showAllPatients() {
        synchronized (lock) {
            String path = "system/Patients.txt";
            List<Patient> patients = readListPatient(path);
            patients.forEach(System.out::print);
        }
        System.out.println("-".repeat(100));
    }

    public void addPatient() {
        String path = "system/Patients.txt";
        Patient patient = new Patient();
        int id = 0;

        synchronized (lock) {
            patient.init();
            List<Patient> patients = readListPatient(path);
            for (Patient p : patients) {
                id = Math.max(id, Integer.parseInt(p.getId()) + 1);
            }
            patient.setId(Integer.toString(id));
            patients.add(patient);
            patients.sort(Patient::compareTo);
            writeListPatient(patients, path);
        }
    }

    public void updatePatient() {
        String path = "system/Patients.txt";
        Patient patient = new Patient();

        synchronized (lock) {
            List<Patient> patients = readListPatient(path);
            patient.update();
            patients.add(patient);
            patients.sort(Patient::compareTo);
            writeListPatient(patients, path);
        }
    }

    public void deletePatient() {
        Scanner sc = new Scanner(System.in);
        Patient patient = new Patient();
        String path = "system/Patients.txt";

        synchronized (lock) {
            List<Patient> patients = readListPatient(path);
            System.out.println(
                    "Выберите одно из указанных действий, введя соответствующее значение: \n" +
                            "1 - найти пациента по номеру полиса\n" +
                            "2 - найти пациента по основным атрибутам\n" +
                            "0 - вернуться в главное меню пациента");
            int inputValue = sc.nextInt();
            if (inputValue == 1) {
                System.out.println("Введите номер полиса удаляемого пациента");
                String police = sc.nextLine();
                for (int i = 0; i < patients.size(); i++) {
                    if (patients.get(i).getInsurancePolicy().equals(police)) {
                        Patient patientToDelete = patients.get(i);
                        patients.remove(patientToDelete);
                        System.out.println("Удален пациент " + patientToDelete.toString());
                    }
                }
            } else if (inputValue == 2) {
                patient.init();
                for (int i = 0; i < patients.size(); i++) {
                    if (patients.get(i).equals(patient)) {
                        Patient patientToDelete = patients.get(i);
                        patients.remove(patient);
                        System.out.println("Удален пациент " + patientToDelete.toString());
                    }
                }
            }
        }
    }

    public void addDoctor() {

    }

    public void addAppointment() {

    }

    public void updateDoctor() {

    }

    public void updateAppointment() {

    }

    public void deleteDoctor() {

    }

    public void deleteAppointment() {

    }

    private List<Patient> readListPatient(String path) {
        String str;
        List<Patient> patients = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            while ((str = br.readLine()) != null) {
                String[] arr = str.split(" ");
                patients.add(new Patient(arr));
            }
        } catch (IOException e) {
            System.out.println("Проблемы при работе с файлом");
        }
        return patients;
    }

    private void writeListPatient(List<Patient> patients, String path) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            for (Patient patient : patients) {
                bw.write(patient.toString());
            }
        } catch (IOException e) {
            System.out.println("Ошибка при записи данных в файл");
        }
    }
}
