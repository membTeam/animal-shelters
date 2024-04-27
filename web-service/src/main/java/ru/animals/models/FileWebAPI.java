package ru.animals.models;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import ru.animals.entities.Animals;
import ru.animals.entities.commonModel.MetaDataPhoto;
import ru.animals.entities.enumEntity.EnumTypeAnimation;
import ru.animals.repository.AnimalsRepository;
import ru.animals.service.AnimalServiceExt;

/**
 * Заполнение структуры объекта Animals в RestAPI в два этапа:
 * WebDTO preparationAnimalData предварительное заполнение полей
 * void preparationAnimalData   заключительная операция
 */
public class FileWebAPI {
    public static WebDTO preparationAnimalData(AnimalServiceExt animalServiceExt, WebAnimal webAnimal) {

        var breedsRepository = animalServiceExt.getBreedsRepository();

        var optionalBreed = breedsRepository.findById(webAnimal.getBreedId());
        if (optionalBreed.isEmpty()) {
            return new WebDTO("internal error.\nThere is no data on the breed of the animal");
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
                .result(true)
                .mesError("ok")
                .photo(photo)
                .animals(animals)
                .metaDataPhoto(metaDataPhoto)
                .breeds(optionalBreed.get())
                .fileExtension(fileExtension.get())
                .build();
    }

    public static void preparationAnimalData(AnimalServiceExt animalServiceExt, WebDTO webDTO) {

        Path imageStorageDir = animalServiceExt.getImageStorageDir();
        AnimalsRepository animalsRepository = animalServiceExt.getAnimalRepository();
        int port = animalServiceExt.getPort();

        var animal = webDTO.getAnimals();
        var breed = webDTO.getBreeds();

        int typeAnimationsId = Math.toIntExact(webDTO.getBreeds().getTypeAnimationsId());

        final String strPrefix = EnumTypeAnimation
                .getStringTypeAnimation(typeAnimationsId).toLowerCase();

        final String fileExtension = Optional.ofNullable( webDTO.getPhoto().getOriginalFilename())
                .flatMap(FileWebAPI::getFileExtension)
                .orElse("empty");

        if (fileExtension.equals("empty")) {
            animalsRepository.deleteById(animal.getId());

            webDTO.setResult(false);
            webDTO.setMesError("Нет расширения файла");
            return;
        }

        var hashCode = String.format("%d%d",
                breed.getId(), animal.getId() ).hashCode();

        var hashmetadata = hashCode < 0
                ? "animal-" + strPrefix + "-img-" + hashCode
                : "animal-" + strPrefix + "-" + hashCode;

        final String targetFileName = String.format("%s.%s",
                hashmetadata, fileExtension);

        final Path baseStoredDir = imageStorageDir.resolve(strPrefix);
        final Path targetPath = baseStoredDir.resolve(targetFileName);

        try {
            Files.deleteIfExists(targetPath);
        } catch (IOException ex) {
            webDTO.setResult(false);
            webDTO.setMesError("internal error");

            return;
        }

        animal.setHashmetadata(hashmetadata);

        MetaDataPhoto metaDataPhoto = webDTO.getMetaDataPhoto();
        metaDataPhoto.setFile(targetFileName);
        metaDataPhoto.setFilepath(targetPath.toString());
        metaDataPhoto.setOtherinf(hashmetadata);
        metaDataPhoto.setHashcode(hashCode);

        metaDataPhoto.setUrl(String.format("localhost:%d/view-animal/%s",
                port, metaDataPhoto.getOtherinf() ));
    }


    //************ Вспомогательные методы

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
                .filesize(imageFile.getSize())
                .metatype(imageFile.getContentType())
                .build();
    }

    private static Optional<String> getFileExtension(String fileName) {
        final int indexOfLastDot = fileName.lastIndexOf('.');

        if (indexOfLastDot == -1) {
            return Optional.empty();
        } else {
            return Optional.of(fileName.substring(indexOfLastDot + 1));
        }
    }

  /*  private static int randomNumber(int grupAnimal) {

        int min = 10000;
        int max = 20000;
        int diff = max - min;

        Random random = new Random();
        int number = random.nextInt(diff + 1) + min;
        return grupAnimal * 100000 + number;
    }
*/
}
