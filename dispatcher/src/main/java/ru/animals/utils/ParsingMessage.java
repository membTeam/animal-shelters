package ru.animals.utils;

import java.util.Arrays;
import java.util.regex.Pattern;

import ru.animals.utilsDEVL.DataFromParser;
import ru.animals.utilsDEVL.ValueFromMethod;


public class ParsingMessage {

    /*private static final String strPattern = "(\\w+)\\s+-t\\s+(\\w+)\\s+-v\\s+([a-zA-Z]+)"+
            "\\s+-d\\s+([a-zA-Z]+-[a-zA-Z]+.txt)|(\\w+.txt)";
    private static final Pattern pattern = Pattern.compile(strPattern);*/

    public static ValueFromMethod parsingMessage(String strMessage) {

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
