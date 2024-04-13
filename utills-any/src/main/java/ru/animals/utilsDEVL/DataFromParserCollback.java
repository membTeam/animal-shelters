package ru.animals.utilsDEVL;

import lombok.*;
import ru.animals.utilsDEVL.entitiesenum.EnumTypeParamCollback;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class DataFromParserCollback {
    private String command;
    private String parameter;
    private EnumTypeParamCollback enumTypeParameter;

}
