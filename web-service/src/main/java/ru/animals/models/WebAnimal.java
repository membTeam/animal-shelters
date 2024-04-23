package ru.animals.models;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class WebAnimal {
    private Long breedId;
    private String nickname;
}
