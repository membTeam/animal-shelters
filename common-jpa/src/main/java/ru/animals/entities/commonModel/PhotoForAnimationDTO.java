package ru.animals.entities.commonModel;


import lombok.Getter;

import java.util.List;

/**
 * Для формирования мнопочного меню фотографий животных
 */
@Getter
public class PhotoForAnimationDTO {
    private final String shortName;
    private final String breed;
    private final String nickname;
    private final String filePath;

    public PhotoForAnimationDTO(List<String> lsData) {
        shortName = lsData.get(0);
        breed = lsData.get(1);
        nickname = lsData.get(2);
        filePath = lsData.get(3);
    }

}
