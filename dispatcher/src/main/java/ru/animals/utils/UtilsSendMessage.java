package ru.animals.utils;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.animals.utils.parser.ControleService;
import ru.animals.utils.parser.StructForCollbackConfig;
import ru.animals.utils.parser.ParsingFromBaseConfigFile;
import ru.animals.utils.parser.StructForBaseConfig;
import ru.animals.utilsDEVL.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Рессивер параметров конфигурационных файлов
 */
@Service
@Log4j
public class UtilsSendMessage implements ControleService {
    private Map<String, StructForBaseConfig> mapSendMessage = new HashMap<>();
    private Map<String, StructForCollbackConfig> mapCollback = new HashMap<>();

    private boolean ERROR=true;
    private String mesRrror = "ok";

    public boolean isERROR() {
        return ERROR;
    }

    private boolean registerError(ValueFromMethod value) {
        ERROR = !value.RESULT;
        if (ERROR) {
            mesRrror = value.MESSAGE;
        }

        return ERROR;
    }

    public UtilsSendMessage(@Value("${menu.configuration}") String fileConfig,
                            @Value("${collback.configuration}") String fileConfCollback )  {

        // TODO: добавить вывод в логФайл

        fillingMapSendMessage(fileConfig);

        if (!isERROR()) {
            fillingMapCollback(fileConfCollback);
        }

    }

    private void fillingMapCollback(String fileConfCollback) {
        ValueFromMethod<List<String>> resLoadCallBack = loadDataFromConfFile(fileConfCollback);
        if (registerError(resLoadCallBack)) {
            return;
        }

        var resultFilling = ParsingFromBaseConfigFile
                .parsingStrConfComdCollback(this, mapCollback, resLoadCallBack.getValue());

        if (!resultFilling.RESULT) {
            log.error(resultFilling.MESSAGE);
        }
        registerError(resultFilling);
    }

    private void fillingMapSendMessage(String fileConfig) {
        ValueFromMethod<List<String>> resLoadConfig = loadDataFromConfFile(fileConfig);
        if (registerError(resLoadConfig)) {
            return;
        }

        var resultFilling = ParsingFromBaseConfigFile
                .parsingStringConfig(mapSendMessage, resLoadConfig.getValue());

        if (!resultFilling.RESULT) {
            log.error(resultFilling.MESSAGE);
        }

        registerError(resultFilling);
    }


    /**
     * Загрузка строк конфигурационных файлов
     * @param file
     * @return
     */
    private ValueFromMethod loadDataFromConfFile(String file) {
        ValueFromMethod<List<String>> resultLoadData = FileAPI.readConfiguration(file);

        registerError(resultLoadData);
        if (!resultLoadData.RESULT) {
            log.error(resultLoadData.MESSAGE);
        }

        return resultLoadData;
    }

    /**
     * Проверка наличия строки в map структуре основного конфигурационного файла
     * @param strCommand
     * @return true входит в структуру иначе не входит
     */
    @Override
    public boolean isExistsInMapConfig(String strCommand) throws Exception {
        if (isERROR()) {
            throw new Exception("Структура команд не создана");
        }
        return mapSendMessage.containsKey(strCommand);
    }

    /**
     * Верификация вхождения команды в структуру mapCollback
     * @param strCommand
     * @return
     * @throws Exception
     */
    @Override
    public boolean isExitsInMapCollback(String strCommand) throws Exception {
        if (isERROR()) {
            throw new Exception("Структура команд не создана");
        }
        return mapCollback.containsKey(strCommand);
    }

    @Override
    public String getMessageErr() {
        return mesRrror;
    }

    public StructForBaseConfig getStructureCommand(String strCommand) throws Exception {

        if (isERROR()) {
            throw new Exception("Нет данных по командам");
        }

        if (mapSendMessage.containsKey(strCommand)) {
            return mapSendMessage.get(strCommand);
        } else {
            throw new Exception("Команда не определена");
        }
    }

    public StructForBaseConfig getStructureCommand (StructForCollbackConfig comnCallback) throws Exception {
        return getStructureCommand(comnCallback.getParameter());
    }

    public StructForCollbackConfig getStructCommandCollback(String strCommand) throws Exception {
        if (isERROR()) {
            throw new Exception("Нет данных по командам");
        }

        if (mapCollback.containsKey(strCommand)) {
            return mapCollback.get(strCommand);
        } else {
            throw new Exception("Команда не определена");
        }
    }

}
