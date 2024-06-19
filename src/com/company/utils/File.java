package com.company.utils;

import com.company.exceptions.FileException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class File {

    public static <T> List<T> readFile(String path, Function<String[], T> function) {
        String str;
        List<T> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            while ((str = br.readLine()) != null) {
                String[] arr = str.split(", ");
                list.add(function.apply(arr));
            }
            return list;
        } catch (FileNotFoundException e) {
            throw new FileException("Файл с записями не найден");
        } catch (IOException e) {
            throw new FileException("Ошибка при чтении данных из файла");
        }
    }

    public static <T> void writeFile(String path, List<T> list) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            for (T l : list) {
                bw.write(l.toString());
            }
        } catch (FileNotFoundException e) {
            throw new FileException("Файл с записями не найден");
        } catch (IOException e) {
            throw new FileException("Ошибка при записи данных в файл");
        }
    }
}
