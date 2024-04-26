package ru.animals.utils.filePhoto;


import lombok.*;
import ru.animals.entities.ContentReport;
import ru.animals.entities.UserBot;
import ru.animals.entities.commonModel.MetaDataPhoto;
import ru.animals.entities.enumEntity.EnumStatusReport;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilePhotoDTO {
    private boolean result;
    private String mesError;

    private UserBot userBot;
    private ContentReport contentReport;

    /*private Long adoptionId;
    private LocalDate date;

    private String animalDiet;
    private String changeBehavior;
    private String generalWellBeing;

    private EnumStatusReport statusReport;
    private MetaDataPhoto metaDataPhoto;*/

    public void setError(String mes) {
        mesError = mes;
        result = false;
    }
}
