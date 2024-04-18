package ru.animals.utils.parser;

import java.util.List;
import java.util.Map;

import ru.animals.utilsDEVL.ValueFromMethod;
import ru.animals.utilsDEVL.entitiesenum.EnumTypeParamMessage;


public class ParsingMessage {

    public static ValueFromMethod parsingTemplateString(Map<String, StructForBaseConfig> map,
                                                        List<String> lsString) {

        try {
            lsString.stream().forEach(str -> {
                var dataParsing = new StructForBaseConfig();

                var arrFromStr = str.split("##");
                String strTypeMessage = arrFromStr[2].trim();

                var enumTypeMessage = EnumTypeParamMessage.of(strTypeMessage); // initEnumTypeMessage(strTypeMessage);
                if (enumTypeMessage == EnumTypeParamMessage.EMPTY) {
                    try {
                        throw new Exception("Тип сообщения " + strTypeMessage + " не определен");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

                dataParsing.setCommand(arrFromStr[0].trim());
                dataParsing.setTypeCommand(arrFromStr[1].trim());
                dataParsing.setParameter(strTypeMessage);
                dataParsing.setSource(arrFromStr[3].trim());
                dataParsing.setEnumTypeMessage(enumTypeMessage);

                map.put(dataParsing.getCommand(), dataParsing);
            });

            return new ValueFromMethod(true,"ok");

        } catch (Exception e) {
            return new ValueFromMethod(false, e.getMessage());
        }

    }

    /*public static ValueFromMethod parsingTemplateString(String strMessage) {

        ValueFromMethod result;

        StructForBaseConfig dataFromParser = new StructForBaseConfig();

        var arrFromStr = strMessage.split("##");

        dataFromParser.setCommand(arrFromStr[0].trim());
        dataFromParser.setTypeCommand(arrFromStr[1].trim());
        dataFromParser.setParameter(arrFromStr[2].trim());
        dataFromParser.setSource(arrFromStr[3].trim());

        return new ValueFromMethod(dataFromParser);

    }*/
}
