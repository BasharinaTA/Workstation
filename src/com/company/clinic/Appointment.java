package com.company.clinic;

import com.company.exceptions.InvalidInputException;
import com.company.utils.FileUtility;
import com.company.utils.Search;
import com.company.utils.Survey;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Appointment extends Entity implements Comparable<Appointment> {

    private static final String PATH = "system/appointment.txt";
    private Patient patient;
    private Doctor doctor;
    private String time;

    public Appointment() {
    }

    public Appointment(String[] arr) {
        this.time = arr[0];
        this.doctor = findDoctor(arr[1]);
        this.patient = findPatient(arr[2]);
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(String policy) {
        this.patient = findPatient(policy);
    }

    private Patient findPatient(String policy) {
        if (!policy.matches("^\\d{16}$")) {
            throw new InvalidInputException("Вводимое значение должно состоять из 16 цифр");
        }
        List<Patient> list = FileUtility.readFile("system/patient.txt", Patient::new);
        Patient patient = list.stream().filter(p -> p.getInsurancePolicy().equals(policy)).findFirst().orElse(null);
        if (patient == null) {
            throw new InvalidInputException("Пациента с указанным полисом нет в записях");
        }
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(String code) {
        this.doctor = findDoctor(code);
    }

    private Doctor findDoctor(String code) {
        if (!code.matches("^\\d{13}$")) {
            throw new InvalidInputException("Вводимое значение должно состоять из 13 цифр");
        }
        List<Doctor> doctors = FileUtility.readFile("system/doctor.txt", Doctor::new);
        Doctor doctor = doctors.stream().filter(d -> d.getCode().equals(code)).findFirst().orElse(null);
        if (doctor == null) {
            throw new InvalidInputException("Врача с указанным кодом нет в записях");
        }
        return doctor;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String str) {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        format.setLenient(false);
        try {
            Date time = format.parse(str);
            if (time.before(new Date())) {
                throw new InvalidInputException("Вводимое значение времени должно быть больше текущего");
            }
            if (time.getDay() == 0 || time.getDay() == 6) {
                throw new InvalidInputException("Запись на выходные дни не ведётся");
            }
            if (time.getHours() < 10 || time.getHours() >= 17 || time.getHours() == 13) {
                throw new InvalidInputException("Указанное значение не попадает в промежуток времени приёма");
            }
            if (time.getMinutes() % 15 != 0) {
                throw new InvalidInputException("Время приёма должно быть кратно 15 минутам");
            }
            this.time = str.trim();
        } catch (ParseException e) {
            throw new InvalidInputException("Вводимое значение должно соответствовать формату дд.мм.гггг чч:мм");
        }
    }

    public static void showMenu(Manager manager, Scanner sc) {
        while (true) {
            System.out.println(
                    """
                            Выберите одно из указанных действий, введя соответствующее значение:
                            1 - показать список записей на приём
                            2 - добавить запись на приём
                            3 - редактировать запись на приём
                            4 - отменить запись на приём
                            0 - вернуться в главное меню""");
            switch (sc.nextLine()) {
                case "0" -> {
                    return;
                }
                case "1" -> manager.showEntities(PATH, Appointment::new);
                case "2" -> manager.addEntity(PATH, sc, Appointment::new, Appointment::new);
                case "3" -> manager.updateEntity(PATH, sc, Appointment::new, Appointment.findAppointment());
                case "4" -> manager.deleteEntity(PATH, sc, Appointment::new, Appointment.findAppointment());
                default -> System.out.println("Введено некорректное значение");
            }
        }
    }

    @Override
    public void init(Scanner sc) {
        List<Survey> list = List.of(
                new Survey("Введите номер полиса пациента", this::setPatient),
                new Survey("Введите код врача", this::setDoctor),
                new Survey("Введите время приёма", this::setTime)
        );
        fillEntity(sc, list);
    }

    @Override
    public void update(Scanner sc) {
        List<Survey> list = List.of(
                new Survey("Введите номер полиса пациента. Если изменение не требуется, нажмите \"Enter\"", this::setPatient),
                new Survey("Введите код специалиста. Если изменение не требуется, нажмите \"Enter\"", this::setDoctor),
                new Survey("Введите время приёма. Если изменение не требуется, нажмите \"Enter\"", this::setTime)
        );
        updateEntity(sc, list);
    }

    @Override
    public boolean check(List<Entity> appointments) {
        for (Entity e : appointments) {
            Appointment appointment = (Appointment) e;
            if (doctor.equals(appointment.getDoctor()) && time.equals(appointment.getTime())) {
                return false;
            }
            if (patient.equals(appointment.getPatient()) && time.equals(appointment.getTime())) {
                return false;
            }
        }
        return true;
    }

    public static List<Search<Appointment>> findAppointment() {
        return List.of(
                new Search<>("Введите номер полиса пациента", Appointment::getInsurancePolicy),
                new Search<>("Введите код специалиста", Appointment::getCode),
                new Search<>("Введите время приёма", Appointment::getTime)
        );
    }

    public String getInsurancePolicy() {
        return this.patient.getInsurancePolicy();
    }

    public String getCode() {
        return this.doctor.getCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Appointment appointment = (Appointment) obj;
        return patient.equals(appointment.getPatient()) && doctor.equals(appointment.getDoctor())
                && time.equals(appointment.getTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(patient.hashCode(), doctor.hashCode(), time);
    }

    @Override
    public int compareTo(Appointment appointment) {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        try {
            Date date1 = format.parse(time);
            Date date2 = format.parse(appointment.getTime());
            if (!date1.equals(date2)) {
                return date1.compareTo(date2);
            }
        } catch (ParseException e) {
            System.out.println("Некорректная форма даты");
        }
        if (!doctor.equals(appointment.getDoctor())) {
            return doctor.compareTo(appointment.getDoctor());
        }
        if (!patient.equals(appointment.getPatient())) {
            return patient.compareTo(appointment.getPatient());
        }
        return 0;
    }

    @Override
    public String toString() {
        return time + ", " + doctor.getCode() + ", " + patient.getInsurancePolicy() + "\n";
    }
}
