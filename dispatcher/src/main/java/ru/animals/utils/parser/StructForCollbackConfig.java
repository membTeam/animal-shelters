package ru.animals.utils.parser;

import lombok.*;
import ru.animals.utilsDEVL.entitiesenum.EnumTypeParamCollback;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class StructForCollbackConfig {
    private String command;
    private String parameter;
    private EnumTypeParamCollback enumTypeParameter;

}
