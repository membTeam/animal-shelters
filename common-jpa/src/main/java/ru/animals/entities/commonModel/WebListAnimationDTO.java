package ru.animals.entities.commonModel;

import lombok.Getter;

@Getter
public class WebListAnimationDTO {
    private String breed;
    private String nikname;
    private String url;

    public WebListAnimationDTO(String breed, String nikname, String url) {
        this.breed = breed;
        this.nikname = nikname;
        this.url = url;
    }
}
