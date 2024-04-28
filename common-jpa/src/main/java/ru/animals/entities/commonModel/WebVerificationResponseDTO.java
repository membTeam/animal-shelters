package ru.animals.entities.commonModel;

import lombok.Getter;

import java.util.List;

@Getter
public final class WebVerificationResponseDTO {

    private final long id;
    private final String animalDiet;
    private final String changeBehavior;
    private final String generalWellBeing;
    private final String dateStart;
    private final String dateFinish;
    private final String url;

    public WebVerificationResponseDTO(List<String> lsData) {
        id = Integer.parseInt(lsData.get(0));
        animalDiet = lsData.get(1);
        changeBehavior = lsData.get(2);
        generalWellBeing = lsData.get(3);
        dateStart = lsData.get(4);
        dateFinish = lsData.get(5);
        url = lsData.get(6);
    }

}
