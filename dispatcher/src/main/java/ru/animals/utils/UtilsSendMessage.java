package ru.animals.utils;

import org.springframework.stereotype.Service;
import ru.animals.utilsDEVL.DataFromParser;
import ru.animals.utilsDEVL.FileAPI;
import ru.animals.utilsDEVL.ValueFromMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UtilsSendMessage {
    private Map<String, DataFromParser> mapSendMessage = new HashMap<>();

    private boolean ERROR;

    public boolean isERROR() {
        return ERROR;
    }

    public UtilsSendMessage() {
        ValueFromMethod resultLoadData = FileAPI.readConfiguration("condig-command.txt");

        // TODO: добавить вывод в логФайл
        if (!resultLoadData.RESULT) {
            return;
        }

        var result = ParsingMessage.parsingTemplateString(mapSendMessage, (List<String>) resultLoadData.VALUE);

        ERROR = !result.RESULT;
    }

    public String getSource(String strCommand) throws Exception {

        if (mapSendMessage.size() == 0) {
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
