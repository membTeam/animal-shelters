package ru.animals.models;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import ru.animals.entities.Animals;
import ru.animals.entities.commonModel.MetaDataPhoto;
import ru.animals.service.AnimalServiceExt;


public class FileWebAPI {
    public static WebDTO preparationAnimalData(AnimalServiceExt animalServiceExt, WebAnimal webAnimal) {

        var breedsRepository = animalServiceExt.getBreedsRepository();

        var optBreed = breedsRepository.findById(webAnimal.getBreedId());
        if (optBreed.isEmpty()) {
            return new WebDTO("internal error");
        }

        MetaDataPhoto metaDataPhoto = initMetaDataPhoto(webAnimal);

        Animals animals = initAnimals(webAnimal);
        animals.setMetaDataPhoto(metaDataPhoto);

        MultipartFile photo = webAnimal.getPhoto();
        var fileExtension = getFileExtension(photo.getOriginalFilename());
        if (fileExtension.isEmpty()) {
            return new WebDTO("Illegal argument for file");
        }

        return WebDTO.builder()
                .animals(animals)
                .metaDataPhoto(metaDataPhoto)
                .breeds(optBreed.get())
                .fileExtension(fileExtension.get())
                .build();
    }

    public static void preparationAnimalData(AnimalServiceExt animalServiceExt, WebDTO webDTO) {

        Path imageStorageDir = animalServiceExt.getImageStorageDir();
        int port = animalServiceExt.getPort();

        final String strPrefix = webDTO.getBreeds().getTypeAnimationsId() == 1 ? "dog" : "cat";
        final String fileExtension = Optional.ofNullable( webDTO.getTargetFileName())
                .flatMap(FileWebAPI::getFileExtension)
                .orElse("");

        final String targetFileName = String.format("%s%d-%d.%s",
                strPrefix, webDTO.getBreeds().getId(), webDTO.getSavedAnimal().getId(), fileExtension);

        final Path baseStoredDir = imageStorageDir.resolve(strPrefix);
        final Path targetPath = baseStoredDir.resolve(targetFileName);

        if (Files.exists(targetPath)) {
            try {
                Files.delete(targetPath);
            } catch (IOException ex) {
                webDTO.setResult(false);
                webDTO.setMessage("internal error");
                return;
            }
        }

        webDTO.getMetaDataPhoto().setFile(targetFileName);
        webDTO.getMetaDataPhoto().setFilepath(targetPath.toString());

        MetaDataPhoto metaDataPhoto = webDTO.getMetaDataPhoto();
        metaDataPhoto.setBreed(webDTO.getBreeds().getBreed());

        var strFile = targetFileName.substring(0,targetFileName.lastIndexOf("."));
        metaDataPhoto.setUrl(String.format("localhost:%d/view-animal/", port, strFile ));
    }

    private static Animals initAnimals(WebAnimal webAnimal) {
        return Animals.builder()
                .breedId(webAnimal.getBreedId())
                .status(true)
                .nickname(webAnimal.getNickname())
                .limitations(webAnimal.getLimitations())
                .build();
    }

    private static MetaDataPhoto initMetaDataPhoto(WebAnimal webAnimal) {
        var imageFile = webAnimal.getPhoto();

        return MetaDataPhoto.builder()
                .filepath("empty")
                .filesize(imageFile.getSize())
                .metatype(imageFile.getContentType())
                .file("empty")
                .build();
    }

    public static Optional<String> getFileExtension(String fileName) {
        final int indexOfLastDot = fileName.lastIndexOf('.');

        if (indexOfLastDot == -1) {
            return Optional.empty();
        } else {
            return Optional.of(fileName.substring(indexOfLastDot + 1));
        }
    }
}
