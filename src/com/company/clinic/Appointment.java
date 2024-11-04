package com.company.clinic;

import com.company.exceptions.InvalidInputException;
import com.company.utils.File;
import com.company.utils.Search;
import com.company.utils.Survey;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Appointment extends Entity implements Comparable<Appointment> {

    private static final String PATH = "system/appointment.txt";
    private String time;
    private String code;
    private String doctorLastname;
    private String doctorFirstname;
    private String position;
    private String insurancePolicy;
    private String patientDateOfBirth;
    private String patientLastname;
    private String patientFirstname;

    public Appointment() {
    }

    public Appointment(String[] arr) {
        setTime(arr[0]);
        setCode(arr[1]);
        setDoctorLastname(arr[2]);
        setDoctorFirstname(arr[3]);
        setPosition(arr[4]);
        setInsurancePolicy(arr[5]);
        setPatientDateOfBirth(arr[6]);
        setPatientLastname(arr[7]);
        setPatientFirstname(arr[8]);
    }

    public String getTime() {
        return time;
    }

    public void setTime(String str) {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        format.setLenient(false);
        try {
            Date time = format.parse(str);
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        if (!code.matches("^\\S{36}$")) {
            throw new InvalidInputException("Вводимое значение должно состоять из 36 символов");
        }
        this.code = code;
    }

    public String getDoctorLastname() {
        return doctorLastname;
    }

    public void setDoctorLastname(String doctorLastname) {
        if (!doctorLastname.matches("^\\S+$")) {
            throw new InvalidInputException("Вводимое значение не должно быть пустым или содержать пробелы");
        }
        this.doctorLastname = doctorLastname;
    }

    public String getDoctorFirstname() {
        return doctorFirstname;
    }

    public void setDoctorFirstname(String doctorFirstname) {
        if (!doctorFirstname.matches("^\\S+$")) {
            throw new InvalidInputException("Вводимое значение не должно быть пустым или содержать пробелы");
        }
        this.doctorFirstname = doctorFirstname;
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

    public String getInsurancePolicy() {
        return insurancePolicy;
    }

    public void setInsurancePolicy(String insurancePolicy) {
        if (!insurancePolicy.matches("^\\d{16}$")) {
            throw new InvalidInputException("Вводимое значение должно состоять из 16 цифр");
        }
        this.insurancePolicy = insurancePolicy;
    }

    public String getPatientDateOfBirth() {
        return patientDateOfBirth;
    }

    public void setPatientDateOfBirth(String patientDateOfBirth) {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        format.setLenient(false);
        try {
            format.parse(patientDateOfBirth);
            this.patientDateOfBirth = patientDateOfBirth.trim();
        } catch (ParseException e) {
            throw new InvalidInputException("Вводимое значение должно соответствовать формату дд.мм.гггг");
        }
    }

    public String getPatientLastname() {
        return patientLastname;
    }

    public void setPatientLastname(String patientLastname) {
        if (!patientLastname.matches("^\\S+$")) {
            throw new InvalidInputException("Вводимое значение не должно быть пустым или содержать пробелы");
        }
        this.patientLastname = patientLastname;
    }

    public String getPatientFirstname() {
        return patientFirstname;
    }

    public void setPatientFirstname(String patientFirstname) {
        if (!patientFirstname.matches("^\\S+$")) {
            throw new InvalidInputException("Вводимое значение не должно быть пустым или содержать пробелы");
        }
        this.patientFirstname = patientFirstname;
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
                case "1" -> manager.showEntities(PATH, sc, Appointment::new, createQuestionsFindAppointment());
                case "2" -> manager.addEntity(PATH, sc, Appointment::new, Appointment::new);
                case "3" -> manager.updateEntity(PATH, sc, Appointment::new, createQuestionsFindAppointment());
                case "4" -> manager.deleteEntity(PATH, sc, Appointment::new, createQuestionsFindAppointment());
                default -> System.out.println("Введено некорректное значение");
            }
        }
    }

    @Override
    public void init(Scanner sc) {
        List<Survey> list = createList(":");
        fillEntity(sc, list);
        checkData();
    }

    @Override
    public void update(Scanner sc) {
        List<Survey> list = createList(". Если изменение не требуется, нажмите \"Enter\"");
        updateEntity(sc, list);
        checkData();
    }

    private List<Survey> createList(String addition) {
        return List.of(
                new Survey("Введите номер полиса пациента" + addition, this::setInsurancePolicy),
                new Survey("Введите код специалиста" + addition, this::setCode),
                new Survey("Введите время приёма" + addition, this::setTime)
        );
    }

    private void checkData() {
        List<Patient> patients = File.readFile("system/patient.txt", Patient::new);
        Patient patient = patients
                .stream()
                .filter(p -> p.getInsurancePolicy().equals(this.insurancePolicy))
                .findFirst()
                .orElse(null);
        if (patient == null) {
            throw new InvalidInputException(String.format("В списке не найден пациент с полисом: %s.", this.insurancePolicy));
        }
        List<Doctor> doctors = File.readFile("system/doctor.txt", Doctor::new);
        Doctor doctor = doctors.stream()
                .filter(d -> d.getCode().equals(this.code))
                .findFirst()
                .orElse(null);
        if (doctor == null) {
            throw new InvalidInputException(String.format("В списке не найден специалист с кодом: %s.", this.code));
        }
        this.setPatientDateOfBirth(patient.getDateOfBirth());
        this.setPatientLastname(patient.getLastname());
        this.setPatientFirstname(patient.getFirstname());
        this.setDoctorLastname(doctor.getLastname());
        this.setDoctorFirstname(doctor.getFirstname());
        this.setPosition(doctor.getPosition());
    }

    @Override
    public boolean check(List<Entity> appointments) {
        for (Entity e : appointments) {
            Appointment appointment = (Appointment) e;
            if (!this.equals(appointment)) {
                if (code.equals(appointment.getCode()) && time.equals(appointment.getTime())) {
                    return false;
                }
                if (insurancePolicy.equals(appointment.getInsurancePolicy()) && time.equals(appointment.getTime())) {
                    return false;
                }
            }
        }
        return true;
    }

    public static List<Search<Appointment>> createQuestionsFindAppointment() {
        return List.of(
                new Search<>("Введите код специалиста:", Appointment::getCode),
                new Search<>("Введите должность специалиста:", Appointment::getPosition),
                new Search<>("Введите фамилию специалиста:", Appointment::getDoctorLastname),
                new Search<>("Введите имя специалиста:", Appointment::getDoctorFirstname),
                new Search<>("Введите дату и время приёма:", Appointment::getTime),
                new Search<>("Введите фамилию пациента:", Appointment::getPatientLastname),
                new Search<>("Введите имя пациента:", Appointment::getPatientFirstname),
                new Search<>("Введите дату рождения пациента:", Appointment::getPatientDateOfBirth),
                new Search<>("Введите номер полиса пациента:", Appointment::getInsurancePolicy)
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
        Appointment appointment = (Appointment) obj;
        return code.equals(appointment.getCode()) && insurancePolicy.equals(appointment.getInsurancePolicy())
                && time.equals(appointment.getTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, insurancePolicy, time);
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
        if (!code.equals(appointment.getCode())) {
            return code.compareTo(appointment.getCode());
        }
        if (!insurancePolicy.equals(appointment.getInsurancePolicy())) {
            return insurancePolicy.compareTo(appointment.getInsurancePolicy());
        }
        return 0;
    }

    @Override
    public String toString() {
        return time + ", " + code + ", " + doctorLastname + ", " + doctorFirstname + ", " + position + ", " +
                insurancePolicy + ", " + patientDateOfBirth + ", " + patientLastname + ", " + patientFirstname + "\n";
    }
}
