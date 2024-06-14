package com.company.clinic;

import com.company.exceptions.InvalidInputException;
import com.company.utils.Survey;
import com.company.utils.Search;

import java.util.*;

public class Doctor extends Person implements Comparable<Doctor> {

    public static final String PATH = "system/doctor.txt";
    private String position;
    private String code;

    public Doctor() {
        code = Long.toString(new Date().getTime());
    }

    public Doctor(String[] arr) {
        super(arr[0], arr[1], arr[2]);
        this.position = arr[3];
        this.code = arr[4];
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        if (!position.matches("[^,]+")) {
            throw new InvalidInputException("Вводимое значение не должно содержать занятые");
        }
        this.position = position.trim();
    }

    public String getCode() {
        return code;
    }

    public static void showMenu(Manager manager, Scanner sc) {
        while (true) {
            System.out.println(
                    """
                            Выберите одно из указанных действий, введя соответствующее значение:
                            1 - показать список специалистов
                            2 - добавить нового специалиста
                            3 - редактировать запись специалиста
                            4 - удалить запись о специалисте
                            0 - вернуться в главное меню""");
            switch (sc.nextLine()) {
                case "0" -> {
                    return;
                }
                case "1" -> manager.showEntities(PATH, Doctor::new);
                case "2" -> manager.addEntity(PATH, sc, Doctor::new, Doctor::new);
                case "3" -> manager.updateEntity(PATH, sc, Doctor::new, Doctor.findDoctor());
                case "4" -> manager.deleteEntity(PATH, sc, Doctor::new, Doctor.findDoctor());
                default -> System.out.println("Введено некорректное значение");
            }
        }
    }

    @Override
    public void init(Scanner sc) {
        List<Survey> list = List.of(
                new Survey("Введите фамилию специалиста", this::setLastname),
                new Survey("Введите имя специалиста", this::setFirstname),
                new Survey("Введите дату рождения специалиста", this::setDateOfBirth),
                new Survey("Введите должность специалиста", this::setPosition)
        );
        fillEntity(sc, list);
    }

    @Override
    public void update(Scanner sc) {
        List<Survey> list = List.of(
                new Survey("Введите фамилию специалиста. Если изменение не требуется, нажмите \"Enter\"", this::setLastname),
                new Survey("Введите имя специалиста. Если изменение не требуется, нажмите \"Enter\"", this::setFirstname),
                new Survey("Введите дату рождения специалиста. Если изменение не требуется, нажмите \"Enter\"", this::setDateOfBirth),
                new Survey("Введите должность специалиста. Если изменение не требуется, нажмите \"Enter\"", this::setPosition)
        );
        updateEntity(sc, list);
    }

    @Override
    public boolean check(List<Entity> doctors) {
        for (Entity e : doctors) {
            Doctor doctor = (Doctor) e;
            if (code.equals(doctor.getCode())) {
                return false;
            }
        }
        return true;
    }

    public static List<Search<Doctor>> findDoctor() {
        return List.of(
                new Search<>("Введите должность специалиста", Doctor::getPosition),
                new Search<>("Введите фамилию специалиста", Doctor::getLastname),
                new Search<>("Введите имя специалиста", Doctor::getFirstname)
        );
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
        return code.equals(doctor.getCode()) && position.equals(doctor.getPosition()) && super.equals(doctor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), position, code);
    }

    @Override
    public int compareTo(Doctor doctor) {
        if (!position.equals(doctor.getPosition())) {
            return position.compareTo(doctor.getPosition());
        }
        if (super.compareTo(doctor) != 0) {
            return super.compareTo(doctor);
        }
        return 0;
    }

    @Override
    public String toString() {
        return super.toString() + ", " + position + ", " + code + "\n";
    }
}
