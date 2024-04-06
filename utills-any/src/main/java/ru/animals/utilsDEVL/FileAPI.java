package ru.animals.utilsDEVL;

import ru.animals.utilsDEVL.entitiesenum.EnumTypeFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class FileAPI {

    /**
     * Определение типа файла
     * @param file
     * @return
     */
    private static EnumTypeFile getTypeFile(String file) {
        var index = file.lastIndexOf(".");
        var strType = file.substring(index+1).toLowerCase();
        return switch (strType) {
            case "txt" -> EnumTypeFile.TEXT;
            case "json" -> EnumTypeFile.JSON;
            case "conf" -> EnumTypeFile.CONFIGURATION;

            // TODO: определиться с типами этой категории файлов
            case "image" -> EnumTypeFile.IMAGE;
            default -> EnumTypeFile.NONE;
        };
    }

    /**
     * Корневая директория
     * @return
     */
    private static Path getRootPath() {
        var userDir = System.getProperty("user.dir");
        Path pathFile;
        if (!userDir.endsWith("animals")) {
            pathFile = Path.of(userDir).getParent();
        } else {
            pathFile = Path.of(userDir);
        }

        return pathFile;
    }

    private static Path pathObjForFile (String file) {

        var dataText = "data-text/";

        var pathDirTextFile = switch (getTypeFile(file)) {
            case TEXT -> dataText + "text-data/";
            case JSON -> dataText + "json/";
            case CONFIGURATION -> dataText;
            default -> "empty";
        };

        if (pathDirTextFile.equals("empty")) {
            return null;
        }

        pathDirTextFile += file;

        Path pathFile = getRootPath();
        return pathFile.resolve(Path.of(pathDirTextFile));
    }

    /**
     * Считывание конфигурационного файла config-command.txt
     * @return
     */
    public static ValueFromMethod readConfiguration(String file){
        ValueFromMethod result;

        try  {
            var pathFile = pathObjForFile(file);
            if (pathFile == null) {
                throw new Exception("Тип файла не определен");
            }

            List<String> resultList = Files
                    .readAllLines(pathFile)
                    .stream()
                    .filter(str-> str.charAt(0)!='#').toList();

            result = new ValueFromMethod<List<String>>(resultList);

        } catch (IOException e) {
            result = new ValueFromMethod(false, String.format("Нет файла " + file));
        } catch (Exception e) {
            result = new ValueFromMethod(false, String.format(e.getMessage()));
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
