package ru.animals.utils.filePhoto;


import lombok.*;
import ru.animals.entities.Adoption;
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
    private String strDirectoryPath;
    private Adoption adoptional;


    public void setError(String mes) {
        mesError = mes;
        result = false;
    }

    public static FilePhotoDTO getFilePhotoDTO() {
        return FilePhotoDTO.builder()
                .result(true)
                .mesError("ok")
                .build();
    }

}
