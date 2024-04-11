package ru.animals.utilsDEVL;

import lombok.Value;
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

        var artifactid = model.getParent().getArtifactId();

        if (artifactid.equals("spring-boot-starter-parent")) {
            artifactid = model.getArtifactId();
        }

        return artifactid;
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
            case "txt", "inf" -> EnumTypeFile.TEXT;
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
    private static Path getRootPath() throws XmlPullParserException, IOException {
        var userDir = System.getProperty("user.dir");
        var artivactId = rootArtifactID(); // DataFromPomXML.getRootArtifactID();

        Path pathFile;
        if (!userDir.endsWith(artivactId)) {
            pathFile = Path.of(userDir).getParent();
        } else {
            pathFile = Path.of(userDir);
        }

        return pathFile;
    }

    private static Path pathObjForFile (String file) throws XmlPullParserException, IOException {

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

    /**
     * Чтение файлов
     * @param file
     * @return ValueFromMethod
     */
    public static ValueFromMethod readDataFromFile(String file) {

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
