package ru.animals.utilsDEVL;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileAPI {

    private static Path pathObjForFile(String file) {
        var userDir = System.getProperty("user.dir");
        var pathDirTextFile = "data-text/" + file;

        Path pathFile;
        if (!userDir.endsWith("animals")) {
            pathFile = Path.of(userDir).getParent();
        } else {
            pathFile = Path.of(userDir);
        }

        return pathFile.resolve(pathDirTextFile);
    }

    public static ValueFromMethod readDataFromFileExt(String file) {
        ValueFromMethod result;

        var pathFile = pathObjForFile(file);
        try  {
            List<String> allLines = Files.readAllLines(pathFile);

            for (var index = allLines.size()-1; index >= 0; index--) {
                if (allLines.get(index).charAt(0) == '#' || allLines.get(index).isBlank()) {
                    allLines.remove(index);
                }
            }

            result = new ValueFromMethod<List<String>>(allLines);
        } catch (IOException e) {
            result = new ValueFromMethod(false, String.format("Нет файла " + file));
        }

        return result;
    }

    public static ValueFromMethod readDataFromFile(String file) {

        ValueFromMethod result;

        var pathFile = pathObjForFile(file);
        try {
            var txt = new String(Files.readAllBytes(pathFile));
            result = new ValueFromMethod<String>(txt);
        } catch (IOException e) {
            result = new ValueFromMethod(false, String.format("Нет файла " + file));
        }

        return result;
    }

}
