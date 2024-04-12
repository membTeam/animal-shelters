package ru.animals.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.animals.utilsDEVL.DataFromParser;
import ru.animals.utilsDEVL.FileAPI;
import ru.animals.utilsDEVL.ParsingStringFromConfigFile;
import ru.animals.utilsDEVL.ValueFromMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Рессивер параметров входящих SendMessage на основе HashMap
 */
@Service
public class UtilsSendMessage {
    private Map<String, DataFromParser> mapSendMessage = new HashMap<>();

    private boolean ERROR=true;
    private String mesRrror = "ok";

    public boolean isERROR() {
        return ERROR;
    }

    public UtilsSendMessage(@Value("${menu.configuration}") String file)  {
        // TODO: вынести в конфигурационный файл

        ValueFromMethod<List<String>> resultLoadData = FileAPI.readConfiguration(file);

        // TODO: добавить вывод в логФайл
        if (!resultLoadData.RESULT) {
            mesRrror = resultLoadData.MESSAGE;
            ERROR = true;
            return;
        }

        var result =
                ParsingStringFromConfigFile.parsingStringConfig(mapSendMessage, resultLoadData.getValue());

        ERROR = !result.RESULT;
        if (ERROR) {
            mesRrror = result.MESSAGE;
        }
    }

    public DataFromParser getDataCommand(String strCommand) throws Exception {

        if (isERROR()) {
            throw new Exception("Нет данных по командам");
        }

        if (mapSendMessage.containsKey(strCommand)) {
            return mapSendMessage.get(strCommand);
        } else {
            throw new Exception("Команда не определена");
        }
    }

    public String getSource(String strCommand) throws Exception {

        if (isERROR()) {
            throw new Exception("Нет данных по командам");
        }

        if (mapSendMessage.containsKey(strCommand)) {
            String result;
            var obj = mapSendMessage.get(strCommand);
            if (obj.getTypeCommand().equals("file") && obj.getParameter().equals("sendmessage")) {
                return "text-data/" + obj.getSource();
            } else {
                throw new Exception("Обработчик команды не определен");
            }
        } else {
            throw new Exception("Команда не определена");
        }
    }

}
