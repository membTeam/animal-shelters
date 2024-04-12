package ru.animals.utilsDEVL;

import ru.animals.utilsDEVL.entitiesenum.EnumTypeMessage;

import java.util.List;
import java.util.Map;

public class ParsingStringFromConfigFile {

    public static ValueFromMethod parsingStringConfig(Map<String,DataFromParser> map,
                                                        List<String> lsString) {

        try {
            lsString.stream().forEach(str -> {
                var dataParsing = new DataFromParser();

                var arrFromStr = str.split("##");
                String strTypeMessage = arrFromStr[2].trim();

                var enumTypeMessage = EnumTypeMessage.of(strTypeMessage); // initEnumTypeMessage(strTypeMessage);
                if (enumTypeMessage == EnumTypeMessage.EMPTY) {
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
}
