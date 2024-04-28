package ru.animals.entities.commonModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.animals.entities.enumEntity.EnumWebResponseReport;


@Getter
@Setter
@AllArgsConstructor
public class WebResponseResultVerificationDTO {

    private Long id;

    private EnumWebResponseReport enumWebResponseReport;

    private String message;

}
