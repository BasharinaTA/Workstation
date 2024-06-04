package com.company;

import com.company.exception.InvalidInputException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public abstract class Person extends Entity implements Comparable<Person> {
    private String id;
    private String lastname;
    private String firstname;
    private String dateOfBirth;

    public Person() {
    }

    public Person(String id, String lastname, String firstname, String dateOfBirth) {
        this.id = id;
        this.lastname = lastname;
        this.firstname = firstname;
        this.dateOfBirth = dateOfBirth;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        if (!lastname.matches("^\\S+$")) {
            throw new InvalidInputException("Вводимое значение не должно содержать пробелы");
        }
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        if (!firstname.matches("^\\S+$")) {
            throw new InvalidInputException("Вводимое значение не должно содержать пробелы");
        }
        this.firstname = firstname;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        try {
            format.setLenient(false);
            format.parse(dateOfBirth);
            this.dateOfBirth = dateOfBirth.trim();
        } catch (ParseException e) {
            throw new InvalidInputException("Вводимое значение должно соответствовать формату дд.мм.гггг");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Person person = (Person) obj;
        return id.equals(person.id)
                && lastname.equals(person.lastname)
                && firstname.equals(person.firstname)
                && dateOfBirth.equals(person.dateOfBirth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lastname, firstname, dateOfBirth);
    }

    @Override
    public int compareTo(Person person) {
        if (!lastname.equals(person.getLastname())) {
            return lastname.compareTo(person.getLastname());
        }
        if (!firstname.equals(person.getFirstname())) {
            return firstname.compareTo(person.getFirstname());
        }
        if (!dateOfBirth.equals(person.getDateOfBirth())) {
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
            try {
                Date date1 = format.parse(dateOfBirth);
                Date date2 = format.parse(person.getDateOfBirth());
                return date1.compareTo(date2);
            } catch (ParseException e) {
                System.out.println("Некорректная запись даты");
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        return getId() + " "
                + getLastname() + " "
                + getFirstname() + " "
                + getDateOfBirth();
    }
}
