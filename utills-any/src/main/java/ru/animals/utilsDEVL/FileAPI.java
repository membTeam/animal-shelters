package ru.animals.utilsDEVL;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class FileAPI {

    public static ValueFromMethod readDataFromFile(String file) {

        ValueFromMethod result;

        var userDir = System.getProperty("user.dir");
        var pathDirTextFile = "data-text/" + file;

        Path pathFile;
        if (!userDir.endsWith("animals")) {
            pathFile = Path.of(userDir).getParent();
        } else {
            pathFile = Path.of(userDir);
        }

        pathFile = pathFile.resolve(pathDirTextFile);

        try {
            var txt = new String(Files.readAllBytes(pathFile));
            result = new ValueFromMethod<String>(txt);
        } catch (IOException e) {
            result = new ValueFromMethod(false, String.format("Нет файла " + file));
        }

        return result;
    }

}
