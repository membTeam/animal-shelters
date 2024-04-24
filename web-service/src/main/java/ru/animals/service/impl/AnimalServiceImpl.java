package ru.animals.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.animals.controller.AnimalsController;
import ru.animals.entities.Animals;
import ru.animals.entities.Breeds;
import ru.animals.entities.commonModel.MetaDataPhoto;
import ru.animals.models.WebAnimal;
import ru.animals.repository.AnimalsRepository;
import ru.animals.repository.BreedsRepository;
import ru.animals.service.AnimalService;
import ru.animals.utilsDEVL.ValueFromMethod;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.beans.Transient;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class AnimalServiceImpl implements AnimalService {

    private final Path imageStorageDir;
    private final BreedsRepository breedsRepository;
    private final AnimalsRepository animalsRepository;

    public AnimalServiceImpl(@Value("${image-storage-dir}") Path imageStorageDir, BreedsRepository breedsRepository, AnimalsRepository animalsRepository) {
        this.imageStorageDir = imageStorageDir;
        this.breedsRepository = breedsRepository;
        this.animalsRepository = animalsRepository;
    }

    @PostConstruct
    public void ensureDirectoryExists() throws IOException  {
        if (!Files.exists(this.imageStorageDir)) {
            Files.createDirectories(this.imageStorageDir);
        }
    }

    @Override
    public List<Breeds> getListBreeds(Long typeAnimationsId) {
        return breedsRepository.findAllByTypeAnimationsId(typeAnimationsId);
    }

    private MetaDataPhoto initMetaDataPhoto(WebAnimal webAnimal) {
        var imageFile = webAnimal.getPhoto();

        return MetaDataPhoto.builder()
                .filepath("empty")
                .filesize(imageFile.getSize())
                .metatype(imageFile.getContentType())
                .file("empty")
                .build();
    }

    private Animals initAnimals(WebAnimal webAnimal) {
        return Animals.builder()
                .breedId(webAnimal.getBreedId())
                .status(true)
                .nickname(webAnimal.getNickname())
                .limitations(webAnimal.getLimitations())
                .build();
    }

    @Override
    public ValueFromMethod addAnimal(WebAnimal webAnimal) {

        var optBreed = breedsRepository.findById(webAnimal.getBreedId());
        if (optBreed.isEmpty()) {
            return new ValueFromMethod("internal error") ;
        }

        var imageFile = webAnimal.getPhoto();

        MetaDataPhoto metaDataPhoto = initMetaDataPhoto(webAnimal);

        Animals animals = initAnimals(webAnimal);
        animals.setMetaDataPhoto(metaDataPhoto);

        var resSaveAnimal = animalsRepository.save(animals);

        var breed = optBreed.get();
        final String strBreed = breed.getTypeAnimationsId() == 1 ? "dog" : "cat";
        final String fileExtension = Optional.ofNullable(imageFile.getOriginalFilename())
                .flatMap(AnimalServiceImpl::getFileExtension)
                .orElse("");

        final String targetFileName = String.format("%s%d-%d.%s",
                strBreed, webAnimal.getBreedId(), resSaveAnimal.getId(), fileExtension);

        final Path baseStoredDir = imageStorageDir.resolve(strBreed);
        final Path targetPath = baseStoredDir.resolve(targetFileName);

        if (Files.exists(targetPath)) {
            try {
                Files.delete(targetPath);
            } catch (IOException ex) {
                return new ValueFromMethod(false, "internal error.");
            }
        }

        metaDataPhoto.setFile(targetFileName);
        metaDataPhoto.setFilepath(targetPath.toString());

        try (InputStream in = imageFile.getInputStream()) {
            resSaveAnimal.setMetaDataPhoto(metaDataPhoto);
            animalsRepository.save(resSaveAnimal);

            OutputStream out = Files.newOutputStream(targetPath, StandardOpenOption.CREATE);
            in.transferTo(out);
        } catch (IOException e) {
            return new ValueFromMethod("Internal error");
        }

        return new ValueFromMethod(true, targetFileName);
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
