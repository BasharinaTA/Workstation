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
        code = "fdfd";
    }

    public Doctor(String[] arr) {
        super(arr[2], arr[3], arr[1]);
        setPosition(arr[4]);
        setCode(arr[0]);
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        if (!position.matches("[^,]+")) {
            throw new InvalidInputException("Вводимое значение не должно быть пустым или содержать запятые");
        }
        this.position = position.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        if (!code.matches("^\\S{36}$")) {
            throw new InvalidInputException("Вводимое значение должно состоять из 36 символов");
        }
        this.code = code;
    }

    public static void showMenu(Manager manager, Scanner sc) {
        while (true) {
            System.out.println(
                    """
                            Выберите одно из указанных действий, введя соответствующее значение:
                            1 - показать список специалистов
                            2 - добавить нового специалиста
                            3 - редактировать запись специалиста
                            4 - удалить запись специалиста
                            0 - вернуться в главное меню""");
            switch (sc.nextLine()) {
                case "0" -> {
                    return;
                }
                case "1" -> manager.showEntities(PATH, sc, Doctor::new, createQuestionsFindDoctor());
                case "2" -> manager.addEntity(PATH, sc, Doctor::new, Doctor::new);
                case "3" -> manager.updateEntity(PATH, sc, Doctor::new, createQuestionsFindDoctor());
                case "4" -> manager.deleteEntity(PATH, sc, Doctor::new, createQuestionsFindDoctor());
                default -> System.out.println("Введено некорректное значение");
            }
        }
    }

    @Override
    public void init(Scanner sc) {
        List<Survey> list = createList(":");
        fillEntity(sc, list);
    }

    @Override
    public void update(Scanner sc) {
        List<Survey> list = createList(". Если изменение не требуется, нажмите \"Enter\"");
        updateEntity(sc, list);
    }

    private List<Survey> createList(String addition) {
        return List.of(
                new Survey("Введите фамилию специалиста" + addition, this::setLastname),
                new Survey("Введите имя специалиста" + addition, this::setFirstname),
                new Survey("Введите дату рождения специалиста" + addition, this::setDateOfBirth),
                new Survey("Введите должность специалиста" + addition, this::setPosition)
        );
    }

    @Override
    public boolean check(List<Entity> doctors) {
        for (Entity e : doctors) {
            Doctor doctor = (Doctor) e;
            if (!this.equals(doctor) && code.equals(doctor.getCode())) {
                return false;
            }
        }
        return true;
    }

    public static List<Search<Doctor>> createQuestionsFindDoctor() {
        return List.of(
                new Search<>("Введите должность специалиста:", Doctor::getPosition),
                new Search<>("Введите фамилию специалиста:", Doctor::getLastname),
                new Search<>("Введите имя специалиста:", Doctor::getFirstname),
                new Search<>("Введите код специалиста:", Doctor::getCode)
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
        return code + ", " + super.toString() + ", " + position + "\n";
    }
}
