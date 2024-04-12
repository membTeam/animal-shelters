package ru.animals.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.animals.utilsDEVL.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Рессивер параметров входящих SendMessage на основе HashMap
 */
@Service
public class UtilsSendMessage {
    private Map<String, DataFromParser> mapSendMessage = new HashMap<>();
    private Map<String, DataFromParserForCollback> mapCollback = new HashMap<>();

    private boolean ERROR=true;
    private String mesRrror = "ok";

    public boolean isERROR() {
        return ERROR;
    }

    public UtilsSendMessage(@Value("${menu.configuration}") String fileConfig,
                            @Value("${collback.configuration}") String fileConfCollback )  {

        // TODO: добавить вывод в логФайл
        ValueFromMethod<List<String>> resLoadConfig = loadDataFromConfFile(fileConfig);
        if (registerError(resLoadConfig)) {
            return;
        }
        var listFromConfFile = resLoadConfig.getValue();

        ValueFromMethod<List<String>> resLoadCallBack = loadDataFromConfFile(fileConfCollback);
        if (registerError(resLoadCallBack)) {
            return;
        }
        var listFromConfCollback = resLoadCallBack.getValue();


        var result = ParsingStringFromConfigFile
                        .parsingStringConfig(mapSendMessage, listFromConfFile);
        if (registerError(result)) {
            return;
        }

        var resultBollback = ParsingStringFromConfigFile
                        .parsingStrConfigComdCollback(mapCollback, listFromConfCollback);
        registerError(resultBollback);
    }

    private boolean registerError(ValueFromMethod value) {
        ERROR = !value.RESULT;
        if (ERROR) {
            mesRrror = value.MESSAGE;
        }

        return ERROR;
    }

    private ValueFromMethod loadDataFromConfFile(String file) {
        ValueFromMethod<List<String>> resultLoadData = FileAPI.readConfiguration(file);
        if (!resultLoadData.RESULT) {
            mesRrror = resultLoadData.MESSAGE;
            ERROR = true;
            return new ValueFromMethod(false, resultLoadData.MESSAGE);
        }

        return resultLoadData;
    }


    public DataFromParser getStructureCommand(String strCommand) throws Exception {

        if (isERROR()) {
            throw new Exception("Нет данных по командам");
        }

        if (mapSendMessage.containsKey(strCommand)) {
            return mapSendMessage.get(strCommand);
        } else {
            throw new Exception("Команда не определена");
        }
    }

}
