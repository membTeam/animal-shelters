package ru.animals.utils;

import java.util.List;
import java.util.Map;

import ru.animals.utilsDEVL.DataFromParser;
import ru.animals.utilsDEVL.ValueFromMethod;


public class ParsingMessage {

    /*private static final String strPattern = "(\\w+)\\s+-t\\s+(\\w+)\\s+-v\\s+([a-zA-Z]+)"+
            "\\s+-d\\s+([a-zA-Z]+-[a-zA-Z]+.txt)|(\\w+.txt)";
    private static final Pattern pattern = Pattern.compile(strPattern);*/

    public static ValueFromMethod parsingTemplateString(Map<String, DataFromParser> map, List<String> lsString) {

        try {
            lsString.stream().forEach(str -> {
                var dataParsing = new DataFromParser();

                var arrFromStr = str.split("##");

                dataParsing.setCommand(arrFromStr[0].trim());
                dataParsing.setTypeCommand(arrFromStr[1].trim());
                dataParsing.setParameter(arrFromStr[2].trim());
                dataParsing.setSource(arrFromStr[3].trim());

                map.put(dataParsing.getCommand(), dataParsing);
            });

            return new ValueFromMethod(true,"ok");

        } catch (Exception e) {
            return new ValueFromMethod(false, e.getMessage());
        }

    }

    public static ValueFromMethod parsingTemplateString(String strMessage) {

        ValueFromMethod result;
        DataFromParser dataFromParser = new DataFromParser();

        var arrFromStr = strMessage.split("##");

        dataFromParser.setCommand(arrFromStr[0].trim());
        dataFromParser.setTypeCommand(arrFromStr[1].trim());
        dataFromParser.setParameter(arrFromStr[2].trim());
        dataFromParser.setSource(arrFromStr[3].trim());

        return new ValueFromMethod(dataFromParser);

    }
}
