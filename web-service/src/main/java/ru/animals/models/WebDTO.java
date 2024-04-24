package ru.animals.models;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.animals.entities.Animals;
import ru.animals.entities.commonModel.MetaDataPhoto;

import java.nio.file.Path;

@Builder
@Getter
@Setter
@NoArgsConstructor
public class WebDTO {
    private Animals animals;
    private MetaDataPhoto metaDataPhoto;
    private String strBreed;
    private Path targetPath;
    private String targetFileName;
}
