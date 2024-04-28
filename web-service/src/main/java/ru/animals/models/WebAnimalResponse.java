package ru.animals.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class WebAnimalResponse {
    private String breed;
    private String nickname;
    private String urlPath;
}
