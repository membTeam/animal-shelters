package ru.animals.utilsDEVL;

import lombok.*;

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
