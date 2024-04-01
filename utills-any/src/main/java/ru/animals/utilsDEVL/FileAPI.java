package ru.animals.utilsDEVL;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileAPI {

    public static ValueFromMethod readDataFromFile(String file) {

        ValueFromMethod result;

        String filePath = "data-text/" + file;

        try {
            var txt = new String(Files.readAllBytes(Paths.get(filePath)));
            result = new ValueFromMethod<String>(txt);
        } catch (IOException e) {
            result = new ValueFromMethod(false, String.format("Нет файла " + file));
        }

        return result;
    }

}
