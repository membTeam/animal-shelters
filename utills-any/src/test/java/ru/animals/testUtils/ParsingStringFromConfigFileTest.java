package ru.animals.testUtils;

import org.junit.jupiter.api.Test;
import ru.animals.utilsDEVL.DataFromParser;
import ru.animals.utilsDEVL.ParsingStringFromConfigFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParsingStringFromConfigFileTest {

    @Test
    public void parsingStringConfig() {
        Map<String, DataFromParser> map = new HashMap<>();
        var str = "documents ## file ## sendmessage ## documents.txt ## документы необходимые, чтобы взять";
        List<String> lsString = List.of(str);

        var result = ParsingStringFromConfigFile.parsingStringConfig(map, lsString);

        assertTrue(result.RESULT);
    }

    @Test
    public void parsingStringConfig_for_dbvolunteers() {
        Map<String, DataFromParser> map = new HashMap<>();
        var str = "dbvolunteers ## fromdbase ## dbvolunteers ## VolunteersServiceImpl.contactsVoluteers ## список";
        List<String> lsString = List.of(str);

        var result = ParsingStringFromConfigFile.parsingStringConfig(map, lsString);

        assertTrue(result.RESULT);
    }




}
