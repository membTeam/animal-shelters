package ru.animals.utils;

import org.springframework.stereotype.Service;
import ru.animals.utilsDEVL.DataFromParser;
import ru.animals.utilsDEVL.FileAPI;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ParsingCommand {
    private Map<String, DataFromParser> mapSendMessage = new HashMap<>();

    public ParsingCommand() {
        var resultLoad = FileAPI.readDataFromFileExt("condig-command.txt");

        // TODO: добавить вывод в логФайл
        if (!resultLoad.RESULT) {
            return;
        }

        var ls = (List<String>) resultLoad.VALUE;
        ls.stream().forEach(item -> {
                    var parsingMessage = ParsingMessage.parsingMessage(item);
                    if (!parsingMessage.RESULT) {
                        return;
                    }

                    var value = (DataFromParser) parsingMessage.VALUE;
                    mapSendMessage.put(value.getCommand(), value);
                }
        );
    }

    public String getSource(String strCommand) {
        if (mapSendMessage.containsKey(strCommand)) {
            String result;
            var obj = mapSendMessage.get(strCommand);
            if (obj.getTypeCommand().equals("file") && obj.getParameter().equals("sendmessage")) {
                return "text-data/" + obj.getSource();
            } else {
                return "empty";
            }
        } else {
            return "empty";
        }
    }

}
