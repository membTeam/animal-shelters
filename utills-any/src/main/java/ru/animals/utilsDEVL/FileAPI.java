package ru.animals.utilsDEVL;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import ru.animals.utilsDEVL.entitiesenum.EnumTypeFile;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileAPI {

    public static String rootArtifactID() throws IOException, XmlPullParserException {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model = reader.read(new FileReader("pom.xml"));

        return model.getParent().getGroupId();
    }

    public static boolean isExistsFile(String file) throws XmlPullParserException, IOException {
        Path path = pathObjForFile(file);
        return Files.exists(path);
    }


    /**
     * Определение типа файла
     * @param file
     * @return
     */
    public static EnumTypeFile getTypeFile(String file) {
        var index = file.lastIndexOf(".");
        var strType = file.substring(index+1).toLowerCase();
        return switch (strType) {
            case "txt" -> EnumTypeFile.TXT;
            case "inf" -> EnumTypeFile.INF;
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
    public static Path getRootPath() throws XmlPullParserException, IOException {
        var userDir = System.getProperty("user.dir");
        var artivactId = rootArtifactID(); // DataFromPomXML.getRootArtifactID();

        Path pathFile = artivactId.equals("ru.animals")
                ? Path.of(userDir).getParent()
                : Path.of(userDir);

        return pathFile;
    }

    private static Path pathObjForFile (String file) throws XmlPullParserException, IOException {

        var dataText = "data-text/";

        var pathDirTextFile = switch (getTypeFile(file)) {
            case TXT, INF -> dataText + "text-data/";
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
    public static ValueFromMethod<List<String>> readConfiguration(String file){
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

    /**
     * Чтение файлов
     * @param file
     * @return ValueFromMethod
     */
    public static ValueFromMethod<String> readDataFromFile(String file) {

        ValueFromMethod result;

        try {
            var pathFile = pathObjForFile(file);

            var txt = new String(Files.readAllBytes(pathFile));
            result = new ValueFromMethod<String>(txt);

        } catch (Exception e) {
            result = new ValueFromMethod(false, String.format("Нет файла " + file));
        }

        return result;
    }

}
