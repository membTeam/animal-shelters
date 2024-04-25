package ru.animals.models;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import ru.animals.entities.Animals;
import ru.animals.entities.Breeds;
import ru.animals.entities.commonModel.MetaDataPhoto;

import java.nio.file.Path;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class WebDTO {
    private boolean result;
    private String message;

    private Animals animals;
    private Animals savedAnimal;
    private Breeds breeds;
    private MetaDataPhoto metaDataPhoto;
    private Path targetPath;
    private String targetFileName;
    private String fileExtension;
    private MultipartFile photo;

    public WebDTO(String err) {
        this.result = false;
        this.message = err;
    }
}
