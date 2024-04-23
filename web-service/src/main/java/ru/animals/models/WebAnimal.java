package ru.animals.models;


import lombok.*;
import ru.animals.entities.enumEntity.EnumLimitations;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class WebAnimal {
    private Long breedId;
    private String nickname;
    private EnumLimitations limitations;
}
