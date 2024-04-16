package ru.animals.utilsStructure;


import lombok.extern.log4j.Log4j;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.animals.telegramComp.TelgramComp;
import ru.animals.utils.UtilsSendMessage;
import ru.animals.utilsDEVL.FileAPI;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Log4j
public class VerifyJSONbtnTest {

    @Autowired
    private UtilsSendMessage utilsSendMessage;

    @Test
    public void verifyStructureJSON() throws Exception {

        var rootPath = FileAPI.getRootPath();
        var localPath = "data-text/json";
        var pathJson = rootPath.resolve(localPath);

        Set<String> setFiles;

        try (Stream<Path> stream = Files.list(pathJson)) {
            setFiles = stream
                    .filter(file -> !Files.isDirectory(file))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toSet());
        }

        List<String> logErr = new ArrayList<>();
        List<String> logJson = new ArrayList<>();

        setFiles.forEach(file -> {
                    var resultData = TelgramComp.sendMessageFromJSON(file);
                    if (!resultData.RESULT) {
                        logErr.add(resultData.MESSAGE);
                        return;
                    }

                    SendMessage telegrComp = resultData.getValue();
                    ReplyKeyboard repl = telegrComp.getReplyMarkup();
                    logJson.add(repl.toString());

                    // проверка заголовочного файла
                    var text = telegrComp.getText();
                    if (text.startsWith("file:")) {
                        var index = text.indexOf(":") + 1;
                        var fileInf = text.substring(index);

                        try {
                            if (!FileAPI.isExistsFile(fileInf)) {
                                log.error(fileInf + " не найден");
                            }
                        } catch (Exception e) {
                        }
                    }
                }
        );

        assertTrue(logErr.size() == 0);
        assertTrue(setFiles.size() > 0);

        // считывание структуры callbackData
        List<String> lsResult = new ArrayList<>();
        logJson.forEach(json -> {
            lsResult.addAll(Arrays.stream(json
                            .split(", "))
                    .filter(str -> str.startsWith("callbackData="))
                    .map(str -> str.substring(str.indexOf("=") + 1))
                    .toList());
        });

        System.out.println("Проверка считывания структуры callbackData");
        assertTrue(lsResult.size() > 0);

        // Проверка вхождения команды в mapCollback -> запись в log file
        lsResult.forEach(strComnd -> {
            try {
                if (!utilsSendMessage.isExitsInMapCollback(strComnd)) {
                    var strErr = strComnd + " нет в mapCollback";
                    logErr.add(strErr);
                    log.error(strErr);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        System.out.println("Проверка вхождения команды в mapCollback");
        assertTrue(logErr.size() == 0);

    }

}
