package com.company;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public abstract class Person implements Comparable<Person> {
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
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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

    protected void personFill(List<Answer> info) {
        Scanner sc = new Scanner(System.in);
        for (int i = 0; i < info.size(); i++) {
            System.out.println(info.get(i).getQuestion());
            String str = sc.nextLine();
            while (str.isEmpty()) {
                System.out.println("Введённое значение не может быть пустым \n" + info.get(i).getQuestion());
                str = sc.nextLine();
            }
            info.get(i).getConsumer().accept(str);
        }
    }
}
