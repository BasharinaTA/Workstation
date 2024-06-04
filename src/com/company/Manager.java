package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

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
                            "exit - выход из системы");
            switch (sc.nextLine()) {
                case "1" -> Appointment.showMenu(this);
                case "2" -> Patient.showMenu(this);
                case "3" -> Doctor.showMenu(this);
                case "exit" -> {
                    return;
                }
                default -> System.out.println("Введено некорректное значение");
            }
        }
    }

    public void showAllPatients() {
        synchronized (lock) {
            String path = "system/Patients.txt";
            List<Patient> patients = readListEntity(path, Patient::new);
            patients.forEach(System.out::print);
        }
    }

    public void addPatient() {
        String path = "system/Patients.txt";
        Patient patient = new Patient();
        int id = 0;

        synchronized (lock) {
            patient.init();
            List<Patient> patients = readListEntity(path, Patient::new);
            for (Patient p : patients) {
                id = Math.max(id, Integer.parseInt(p.getId()) + 1);
                if (p.getInsurancePolicy().equals(patient.getInsurancePolicy())) {
                    System.out.println("Пациент с указанным полисом уже существует");
                    return;
                }
            }
            patient.setId(Integer.toString(id));
            patients.add(patient);
            patients.sort(Patient::compareTo);
            writeListEntity(path, patients);
            System.out.println("Успешно добавлен пациент: " + patient);
        }
    }

    public void updatePatient() {
        String path = "system/Patients.txt";
        Scanner sc = new Scanner(System.in);

        synchronized (lock) {
            List<Patient> patients = readListEntity(path, Patient::new);
            Patient patient = findPatient(sc, patients);
            if (patient != null) {
                System.out.print("Полученный для редактирования пациент: " + patient);
                patient.update();
                for (Patient p : patients) {
                    if (!p.getId().equals(patient.getId())
                            && p.getInsurancePolicy().equals(patient.getInsurancePolicy())) {
                        System.out.println("Пациент с указанным полисом уже существует");
                       return;
                    }
                }
                patients.sort(Patient::compareTo);
                writeListEntity(path, patients);
                System.out.println("Успешно отредактирован пациент: " + patient);
            } else {
                System.out.println("Пациента с указанным полисом нет в записях");
            }
        }
    }

    private Patient findPatient(Scanner sc, List<Patient> patients) {
        System.out.println("Введите номер полиса пациента");
        String policy = sc.nextLine();
        return patients
                .stream()
                .filter(p -> p.getInsurancePolicy().equals(policy))
                .findFirst().orElse(null);
    }

    public void deletePatient() {
        String path = "system/Patients.txt";
        Scanner sc = new Scanner(System.in);

        synchronized (lock) {
            List<Patient> patients = readListEntity(path, Patient::new);
            Patient patient = findPatient(sc, patients);
            if (patient != null) {
                System.out.println("Вы действительно хотите удалить пациента " + patient
                        + "Если да, введите \"yes\"");
                String answer = sc.nextLine();
                if (!answer.equals("yes")) {
                    return;
                }
                patients.remove(patient);
                writeListEntity(path, patients);
                System.out.println("Успешно удалён пациент: " + patient);
            } else {
                System.out.println("Пациента с указанным полисом нет в записях");
            }
        }
    }

    public void showAllDoctors() {
        String path = "system/Doctors.txt";
        List<Doctor> doctors = readListEntity(path, Doctor::new);
        doctors.forEach(System.out::print);
    }

    public void addDoctor() {
        String path = "system/Doctors.txt";
        Doctor doctor = new Doctor();
        int id = 0;

        synchronized (lock) {
            doctor.init();
            List<Doctor> doctors = readListEntity(path, Doctor::new);
            for (Doctor d : doctors) {
                id = Math.max(id, Integer.parseInt(d.getId()) + 1);
            }
            doctor.setId(Integer.toString(id));
            doctors.add(doctor);
            doctors.sort(Doctor::compareTo);
            writeListEntity(path, doctors);
        }
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

    private <T> List<T> readListEntity(String path, Function<String[], T> function) {
        String str;
        List<T> patients = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            while ((str = br.readLine()) != null) {
                String[] arr = str.split(" ");
                patients.add(function.apply(arr));
            }
        } catch (IOException e) {
            System.out.println("Проблемы при работе с файлом");
        }
        return patients;
    }

    private <T> void writeListEntity(String path, List<T> patients) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            for (T patient : patients) {
                bw.write(patient.toString());
            }
        } catch (IOException e) {
            System.out.println("Ошибка при записи данных в файл");
        }
    }
}
