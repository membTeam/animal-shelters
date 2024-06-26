package ru.animals.parsing;

import org.junit.jupiter.api.Test;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.animals.utils.parser.ParsingFromBaseConfigFile;
import ru.animals.utils.parser.StructForBaseConfig;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParsingStringFromConfigFileTest {

    @Test
    public void parsingStringConfig() {
        Map<String, StructForBaseConfig> map = new HashMap<>();
        var str = "documents ## file ## sendmessage ## documents.txt ## документы необходимые, чтобы взять";
        List<String> lsString = List.of(str);

        var result = ParsingFromBaseConfigFile.parsingStringConfig(map, lsString);

        assertTrue(result.RESULT);
    }

    @Test
    public void parsingStringConfig_for_btmmenu() {
        Map<String, StructForBaseConfig> map = new HashMap<>();
        var str = "infopet ## json ## btmmenu ## info-big.json ## информация для усыновления";
        List<String> lsString = List.of(str);

        var result = ParsingFromBaseConfigFile.parsingStringConfig(map, lsString);

        assertTrue(result.RESULT);
    }

}
