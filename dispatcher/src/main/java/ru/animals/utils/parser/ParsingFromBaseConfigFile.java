package ru.animals.utils.parser;

import ru.animals.utils.UtilsSendMessageServ;
import ru.animals.utilsDEVL.ValueFromMethod;
import ru.animals.utilsDEVL.entitiesenum.EnumTypeParamMessage;
import ru.animals.utilsDEVL.entitiesenum.EnumTypeParamCollback;

import java.util.List;
import java.util.Map;

/**
 * Парсинг строк конфигурационных файлов data-text/*.conf
 */
public class ParsingFromBaseConfigFile {

    public static ValueFromMethod parsingStringConfig(Map<String, StructForBaseConfig> map,
                                                      List<String> lsString) {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            lsString.stream().forEach(str -> {
                var dataParsing = new StructForBaseConfig();

                var arrFromStr = str.split("##");
                String strTypeMessage = arrFromStr[2].trim();

                var enumTypeMessage = EnumTypeParamMessage.of(strTypeMessage);
                if (enumTypeMessage == EnumTypeParamMessage.EMPTY) {
                    stringBuilder.append("Тип сообщения " + strTypeMessage + " не определен \n");
                    return;
                }

                dataParsing.setCommand(arrFromStr[0].trim());
                dataParsing.setTypeCommand(arrFromStr[1].trim());
                dataParsing.setParameter(strTypeMessage);
                dataParsing.setSource(arrFromStr[3].trim());
                dataParsing.setEnumTypeMessage(enumTypeMessage);

                map.put(dataParsing.getCommand(), dataParsing);
            });

            if (stringBuilder.length() > 0) {
                return new ValueFromMethod(stringBuilder.toString());
            }

            return new ValueFromMethod(true,"ok");

        } catch (Exception e) {
            return new ValueFromMethod(false, e.getMessage());
        }
    }

    public static ValueFromMethod parsingStrConfComdCollback(UtilsSendMessageServ controleService, Map<String, StructForCollbackConfig> map,
                                                             List<String> lsString) {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            lsString.stream().forEach(str -> {

                var arrFromStr = str.split("##");

                var strTypeCommand = arrFromStr[0].trim().substring(0,3);
                var strTypeMessage = arrFromStr[1].trim();

                var enumTypeMessage = EnumTypeParamCollback.of(strTypeCommand);
                if (enumTypeMessage.equals(EnumTypeParamCollback.NONE)) {
                    stringBuilder.append("Тип сообщения: " + strTypeMessage + " не определен \n");
                    return;
                }

                var command = arrFromStr[0].trim();
                var paramenter = arrFromStr[1].trim();

                if (!enumTypeMessage.equals(EnumTypeParamCollback.TCL_DBD)){
                    try {
                        if (!controleService.isExistsInMapConfig(paramenter)) {
                            var err = String.format("%s: %s не определен \n", command, paramenter);
                            stringBuilder.append(err);
                            return;
                        }
                    } catch (Exception e) {
                        return;
                    }
                }

                var dataParsing = new StructForCollbackConfig();
                dataParsing.setCommand(command);
                dataParsing.setParameter (paramenter);
                dataParsing.setEnumTypeParameter(enumTypeMessage);

                map.put(dataParsing.getCommand(), dataParsing);
            });

            if (stringBuilder.length() > 0) {
                return new ValueFromMethod(false, stringBuilder.toString());
            }

            return new ValueFromMethod(true,"ok");

        } catch (Exception e) {
            return new ValueFromMethod(false, e.getMessage());
        }

    }

}
