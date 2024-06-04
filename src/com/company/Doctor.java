package com.company;

import com.company.exception.InvalidInputException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Doctor extends Person {
    private String position;

    public Doctor() {
    }

    public Doctor(String[] arr) {
        super(arr[0], arr[1], arr[2], arr[3]);
        this.position = arr[4];
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        if (position.matches("^\\S+$")) {
            throw new InvalidInputException("Вводимое значение не должно содержать пробелы");
        }
        this.position = position;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Doctor doctor = (Doctor) obj;
        return super.equals(doctor) && position.equals(doctor.getPosition());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), position);
    }

    public int compareTo(Doctor doctor) {
        if (super.compareTo(doctor) != 0) {
            return super.compareTo(doctor);
        }
        if (!position.equals(doctor.getPosition())) {
            return position.compareTo(doctor.getPosition());
        }
        return 0;
    }

    @Override
    public String toString() {
        return super.toString() + " " + position + "\n";
    }

    public static void showMenu(Manager manager) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println(
                    "Выберите одно из указанных действий, введя соответствующее значение:\n" +
                            "1 - показать список специалистов\n" +
                            "2 - добавить нового специалиста\n" +
                            "3 - редактировать запись специалиста\n" +
                            "4 - удалить запись о специалисте\n" +
                            "0 - вернуться в главное меню");
            switch (sc.nextLine()) {
                case "0" -> {
                    return;
                }
                case "1" -> manager.showAllDoctors();
                case "2" -> manager.addDoctor();
                case "3" -> manager.updateDoctor();
                case "4" -> manager.deleteDoctor();
                default -> System.out.println("Введено некорректное значение");
            }
        }
    }

    @Override
    public void init() {
        List<Answer> info = Arrays.asList(
                new Answer("Введите фамилию специалиста", this::setLastname),
                new Answer("Введите имя специалиста", this::setFirstname),
                new Answer("Введите дату рождения специалиста", this::setDateOfBirth),
                new Answer("Введите должность специалиста", this::setPosition)
        );
        fillEntity(info);
    }


    public List<Doctor> find(String position, String lastname, String firstname) {
        return null;
    }

    @Override
    public void update() {
        System.out.println("Обновление записи");
    }

}
