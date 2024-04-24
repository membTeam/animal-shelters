package ru.animals.models;

import lombok.*;
import ru.animals.entities.Animals;
import ru.animals.entities.Breeds;
import ru.animals.entities.commonModel.MetaDataPhoto;

import java.nio.file.Path;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class WebDTO {
    @Getter
    private boolean result = true;
    @Getter
    private String message = "ok";

    private Animals animals;
    private Animals savedAnimal;
    private Breeds breeds;
    private MetaDataPhoto metaDataPhoto;
    private Path targetPath;
    private String targetFileName;
    private String fileExtension;

    public WebDTO(String err) {
        this.result = false;
        this.message = err;
    }
}
