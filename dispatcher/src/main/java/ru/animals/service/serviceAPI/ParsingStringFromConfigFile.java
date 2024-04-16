package ru.animals.service.serviceAPI;

import ru.animals.utilsDEVL.ValueFromMethod;
import ru.animals.utilsDEVL.entitiesenum.EnumTypeParamMessage;
import ru.animals.utilsDEVL.entitiesenum.EnumTypeParamCollback;

import java.util.List;
import java.util.Map;

public class ParsingStringFromConfigFile {

    public static ValueFromMethod parsingStringConfig(Map<String, DataFromParser> map,
                                                      List<String> lsString) {
        try {
            lsString.stream().forEach(str -> {
                var dataParsing = new DataFromParser();

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

    public static ValueFromMethod parsingStrConfComdCollback(ControleService controleService, Map<String, DataFromParserCollback> map,
                                                             List<String> lsString) {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            lsString.stream().forEach(str -> {

                var arrFromStr = str.split("##");

                var strTypeCommand = arrFromStr[0].trim().substring(0,3);
                var strTypeMessage = arrFromStr[1].trim();

                var enumTypeMessage = EnumTypeParamCollback.of(strTypeCommand);
                if (enumTypeMessage.equals(EnumTypeParamCollback.NONE)) {
                    try {
                        stringBuilder.append("Тип сообщения: " + strTypeMessage + " не определен \n");
                        return;
                        //throw new Exception("Тип сообщения " + strTypeMessage + " не определен");
                    } catch (Exception e) {
                        stringBuilder.append(e.getMessage() + "\n");
                        return;
                        //throw new RuntimeException(e);
                    }
                }

                var command = arrFromStr[0].trim();
                var paramenter = arrFromStr[1].trim();

                if (!enumTypeMessage.equals(EnumTypeParamCollback.TCL_DBD)){
                    try {
                        if (!controleService.isExistsInMapConfig(paramenter)) {
                            var err = String.format("%s: %s не определен \n", command, paramenter);
                            stringBuilder.append(err);
                            return;
                            //throw new Exception(err);
                        }
                    } catch (Exception e) {
                        return;
                        //throw new RuntimeException(e);
                    }
                }

                var dataParsing = new DataFromParserCollback();
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
