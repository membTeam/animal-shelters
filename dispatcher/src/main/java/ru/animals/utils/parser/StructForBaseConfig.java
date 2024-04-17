package ru.animals.utils.parser;

import lombok.*;
import ru.animals.utils.parser.enumType.EnumTypeStructConf;
import ru.animals.utilsDEVL.entitiesenum.EnumTypeParamMessage;


@AllArgsConstructor
@Setter
@Getter
@Builder
public class StructForBaseConfig {
    private String command;
    private EnumTypeStructConf typeCommand;
    private String parameter;
    private String source;
    private String helpData;
    private EnumTypeParamMessage enumTypeMessage;


    public StructForBaseConfig(){}

    @Override
    public String toString() {
        return "DataFromParser\n" +
                "\tcommand: " + command + '\n' +
                "\ttypeCommand: " + typeCommand + '\n' +
                "\tparameter: " + parameter + '\n' +
                "\tsource: " + source + '\n' +
                "\thelpData: " + helpData ;
    }
}
