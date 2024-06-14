package com.company.clinic;

import com.company.utils.FileUtility;
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
                            1 - запись на приём
                            2 - работа с пациентом
                            3 - работа со специалистом
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

    public <T extends Entity> void showEntities(String path,
                                                Function<String[], T> function) {
        synchronized (lock) {
            List<T> entities = FileUtility.readFile(path, function);
            entities.forEach(System.out::print);
        }
    }

    public <T extends Entity> void addEntity(String path,
                                             Scanner sc,
                                             Supplier<T> supplier,
                                             Function<String[], T> function) {
        T entity = supplier.get();
        synchronized (lock) {
            entity.init(sc);
            List<T> entities = FileUtility.readFile(path, function);
            if (!entity.check((List<Entity>) entities)) {
                System.out.println("Запись с указанными данными уже существует");
                return;
            }
            entities.add(entity);
            entities = entities.stream().sorted().collect(Collectors.toList());
            FileUtility.writeFile(path, entities);
            System.out.printf("Успешно добавлен запись: %s", entity);
        }
    }

    public <T extends Entity> void updateEntity(String path,
                                                Scanner sc,
                                                Function<String[], T> function,
                                                List<Search<T>> list) {
        synchronized (lock) {
            List<T> entities = FileUtility.readFile(path, function);
            T entity = findEntity(sc, entities, list);
            if (entity != null) {
                System.out.printf("Полученная для редактирования запись: %s", entity);
                entity.update(sc);
                entities = entities.stream().sorted().collect(Collectors.toList());
                FileUtility.writeFile(path, entities);
                System.out.printf("Успешно отредактирована запись: %s ", entity);
            } else {
                System.out.println("Записи с указанными данными нет в списке");
            }
        }
    }

    public <T extends Entity> void deleteEntity(String path,
                                                Scanner sc,
                                                Function<String[], T> function,
                                                List<Search<T>> list) {
        synchronized (lock) {
            List<T> entities = FileUtility.readFile(path, function);
            T entity = findEntity(sc, entities, list);
            if (entity != null) {
                System.out.printf(
                        """
                                Вы действительно хотите удалить запись: %sЕсли да, введите "yes" и нажмите "Enter"
                                Если нет, нажмите "Enter"
                                """, entity);
                if (!sc.nextLine().equals("yes")) {
                    System.out.printf("Не была удалена запись: %s", entity);
                    return;
                }
                entities.remove(entity);
                FileUtility.writeFile(path, entities);
                System.out.printf("Успешно удалена запись: %s ", entity);
            } else {
                System.out.println("В списке не найдена запись с указанными данными");
            }
        }
    }

    private <T> T findEntity(Scanner sc, List<T> entities, List<Search<T>> arr) {
        for (Search<T> a : arr) {
            System.out.println(a.getQuestion());
            String data = sc.nextLine();
            entities = entities
                    .stream()
                    .filter(d -> a.getFunction().apply(d).equals(data))
                    .collect(Collectors.toList());
            if (entities.size() <= 1) {
                return entities.stream().findFirst().orElse(null);
            }
            entities.forEach(System.out::print);
        }
        throw new RuntimeException("many id in doctors");
    }
}
