package com.company.clinic;

import com.company.exceptions.InvalidInputException;
import com.company.utils.File;
import com.company.utils.Search;

import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Manager {

    private static final Object lock = new Object();

    public void mainMenu(Scanner sc) {
        while (true) {
            System.out.println(
                    """
                            Выберите одно из указанных действий, введя соответствующее значение:
                            1 - работа со списком приёмов
                            2 - работа со списком пациентов
                            3 - работа со списком специалистов
                            exit - выход из системы""");
            switch (sc.nextLine()) {
                case "1" -> Appointment.showMenu(this, sc);
                case "2" -> Patient.showMenu(this, sc);
                case "3" -> Doctor.showMenu(this, sc);
                case "exit" -> {
                    return;
                }
                default -> System.out.println("Введено некорректное значение");
            }
        }
    }

    public <T> void showEntities(String path,
                                 Scanner sc,
                                 Function<String[], T> function,
                                 List<Search<T>> list) {
        synchronized (lock) {
            List<T> entities = File.readFile(path, function);
            entities.forEach(System.out::print);
            filterEntities(sc, entities, list);
        }
    }

    private <T> void filterEntities(Scanner sc, List<T> entities, List<Search<T>> arr) {
        while (true) {
            System.out.println("Если хотите дополнительно отфильтровать записи, введите \"да\".");
            if (!sc.nextLine().trim().equals("да")) {
                return;
            }
            for (Search<T> a : arr) {
                System.out.println(a.getQuestion());
                String data = sc.nextLine();
                entities = entities
                        .stream()
                        .filter(d -> a.getFunction().apply(d).contains(data))
                        .collect(Collectors.toList());
                entities.forEach(System.out::print);
                if (entities.size() == 1) {
                    return;
                }
                if (entities.isEmpty()) {
                    System.out.println("В списке не найдена запись с указанными данными");
                    return;
                }
            }
        }
    }

    public <T extends Entity> void addEntity(String path,
                                             Scanner sc,
                                             Supplier<T> supplier,
                                             Function<String[], T> function) {
        T entity = supplier.get();
        try {
            entity.init(sc);
        } catch (InvalidInputException e) {
            System.out.println(e.getMessage() + " Запись не добавлена.");
            return;
        }
        synchronized (lock) {
            List<T> entities = File.readFile(path, function);
            if (!entity.check((List<Entity>) entities)) {
                System.out.println("Запись с указанными данными уже существует");
                return;
            }
            entities.add(entity);
            entities = entities.stream().sorted().collect(Collectors.toList());
            File.writeFile(path, entities);
            System.out.printf("Успешно добавлена запись: %s", entity);
        }
    }

    public <T extends Entity> void updateEntity(String path,
                                                Scanner sc,
                                                Function<String[], T> function,
                                                List<Search<T>> list) {
        synchronized (lock) {
            List<T> entities = File.readFile(path, function);
            T entity = findEntity(sc, entities, list);
            if (entity != null) {
                System.out.printf("Полученная для редактирования запись: %s", entity);
                try {
                    entity.update(sc);
                } catch (InvalidInputException e) {
                    System.out.println(e.getMessage() + " Запись не обновлена.");
                    return;
                }
                if (!entity.check((List<Entity>) entities)) {
                    System.out.println("Запись с указанными данными уже существует");
                    return;
                }
                entities = entities.stream().sorted().collect(Collectors.toList());
                File.writeFile(path, entities);
                System.out.printf("Успешно отредактирована запись: %s ", entity);
            } else {
                System.out.println("В списке не найдена запись с указанными данными");
            }
        }
    }

    public <T extends Entity> void deleteEntity(String path,
                                                Scanner sc,
                                                Function<String[], T> function,
                                                List<Search<T>> list) {
        synchronized (lock) {
            List<T> entities = File.readFile(path, function);
            T entity = findEntity(sc, entities, list);
            if (entity != null) {
                System.out.printf("Введите \"да\", если хотите удалить запись: %s", entity);
                if (!sc.nextLine().trim().equals("да")) {
                    System.out.printf("Не была удалена запись: %s", entity);
                    return;
                }
                entities.remove(entity);
                File.writeFile(path, entities);
                System.out.printf("Успешно удалена запись: %s ", entity);
            } else {
                System.out.println("В списке не найдена запись с указанными данными");
            }
        }
    }

    public <T> T findEntity(Scanner sc, List<T> entities, List<Search<T>> arr) {
        while (true) {
            for (Search<T> a : arr) {
                System.out.println(a.getQuestion());
                String data = sc.nextLine();
                entities = entities
                        .stream()
                        .filter(d -> a.getFunction().apply(d).contains(data))
                        .collect(Collectors.toList());
                if (entities.size() <= 1) {
                    return entities.stream().findFirst().orElse(null);
                }
                entities.forEach(System.out::print);
            }
        }
    }
}
