package com.company.clinic;

import com.company.exceptions.InvalidInputException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public abstract class Person extends Entity {

    private String lastname;
    private String firstname;
    private String dateOfBirth;

    public Person() {
    }

    public Person(String lastname, String firstname, String dateOfBirth) {
        setLastname(lastname);
        setFirstname(firstname);
        setDateOfBirth(dateOfBirth);
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        if (!lastname.matches("^\\S+$")) {
            throw new InvalidInputException("Вводимое значение не должно быть пустым или содержать пробелы");
        }
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        if (!firstname.matches("^\\S+$")) {
            throw new InvalidInputException("Вводимое значение не должно быть пустым или содержать пробелы");
        }
        this.firstname = firstname;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        format.setLenient(false);
        try {
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
        return lastname.equals(person.lastname)
                && firstname.equals(person.firstname)
                && dateOfBirth.equals(person.dateOfBirth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lastname, firstname, dateOfBirth);
    }

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
        return getDateOfBirth() + ", " + getLastname() + ", " + getFirstname();
    }
}
