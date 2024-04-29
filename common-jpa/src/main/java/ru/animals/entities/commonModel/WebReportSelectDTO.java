package ru.animals.entities.commonModel;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WebReportSelectDTO {
    private Long id;
    private LocalDate dateRep;
    private LocalDate dateStart;
    private LocalDate dateFinish;

    private String animalDiet;
    private String changeBehavior;
    private String generaWellBeing;
}
