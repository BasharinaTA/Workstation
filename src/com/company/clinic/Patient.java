package com.company.clinic;

import com.company.exceptions.InvalidInputException;
import com.company.utils.Survey;
import com.company.utils.Search;

import java.util.*;

public class Patient extends Person implements Comparable<Patient> {

    public static final String PATH = "system/patient.txt";
    private String insurancePolicy;

    public Patient() {
    }

    public Patient(String[] arr) {
        super(arr[2], arr[3], arr[1]);
        setInsurancePolicy(arr[0]);
    }

    public String getInsurancePolicy() {
        return insurancePolicy;
    }

    public void setInsurancePolicy(String insurancePolicy) {
        if (!insurancePolicy.matches("^\\d{16}$")) {
            throw new InvalidInputException("Вводимое значение должно состоять из 16 цифр");
        }
        this.insurancePolicy = insurancePolicy;
    }

    public static void showMenu(Manager manager, Scanner sc) {
        while (true) {
            System.out.println(
                    """
                            Выберите одно из указанных действий, введя соответствующее значение:
                            1 - показать список пациентов
                            2 - добавить нового пациента
                            3 - редактировать запись пациента
                            4 - удалить запись пациента
                            0 - вернуться в главное меню""");
            switch (sc.nextLine()) {
                case "0" -> {
                    return;
                }
                case "1" -> manager.showEntities(PATH, sc, Patient::new, createQuestionsFindPatient());
                case "2" -> manager.addEntity(PATH, sc, Patient::new, Patient::new);
                case "3" -> manager.updateEntity(PATH, sc, Patient::new, createQuestionsFindPatient());
                case "4" -> manager.deleteEntity(PATH, sc, Patient::new, createQuestionsFindPatient());
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
                new Survey("Введите фамилию пациента" + addition, this::setLastname),
                new Survey("Введите имя пациента" + addition, this::setFirstname),
                new Survey("Введите дату рождения пациента" + addition, this::setDateOfBirth),
                new Survey("Введите номер полиса пациента" + addition, this::setInsurancePolicy)
        );
    }

    @Override
    public boolean check(List<Entity> patients) {
        for (Entity e : patients) {
            Patient patient = (Patient) e;
            if (!this.equals(patient) && insurancePolicy.equals(patient.getInsurancePolicy())) {
                return false;
            }
        }
        return true;
    }

    public static List<Search<Patient>> createQuestionsFindPatient() {
        return List.of(
                new Search<>("Введите дату рождения пациента:", Patient::getDateOfBirth),
                new Search<>("Введите фамилию пациента:", Patient::getLastname),
                new Search<>("Введите имя пациента:", Patient::getFirstname),
                new Search<>("Введите номер полиса пациента:", Patient::getInsurancePolicy));
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
        return insurancePolicy.equals(patient.getInsurancePolicy()) && super.equals(patient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), insurancePolicy);
    }

    @Override
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
        return insurancePolicy + ", " + super.toString() + "\n";
    }
}
