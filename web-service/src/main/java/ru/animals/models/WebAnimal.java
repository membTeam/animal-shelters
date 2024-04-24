package ru.animals.models;


import lombok.*;
import ru.animals.entities.enumEntity.EnumLimitations;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class WebAnimal implements Serializable {
    private Long breedId;
    private String nickname;
    private EnumLimitations limitations;

    private MultipartFile photo;
}
