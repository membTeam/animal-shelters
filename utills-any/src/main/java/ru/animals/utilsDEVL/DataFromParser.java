package ru.animals.utilsDEVL;

import lombok.*;
import ru.animals.utilsDEVL.entitiesenum.EnumTypeParamMessage;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class DataFromParser {
    private String command;
    private String typeCommand;
    private String parameter;
    private String source;
    private String helpData;
    private EnumTypeParamMessage enumTypeMessage;

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
