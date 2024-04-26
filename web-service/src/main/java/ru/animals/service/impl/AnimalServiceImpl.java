package ru.animals.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.animals.entities.Animals;
import ru.animals.entities.Breeds;
import ru.animals.models.FileWebAPI;
import ru.animals.models.WebAnimal;
import ru.animals.models.WebDTO;
import ru.animals.repository.AnimalsRepository;
import ru.animals.repository.BreedsRepository;
import ru.animals.service.AnimalService;
import ru.animals.service.AnimalServiceExt;
import ru.animals.utilsDEVL.ValueFromMethod;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

@Service
public class AnimalServiceImpl implements AnimalService, AnimalServiceExt {

    private final Path imageStorageDir;
    private final BreedsRepository breedsRepository;
    private final AnimalsRepository animalsRepository;

    private final int port;

    public AnimalServiceImpl(@Value("${image-storage-dir}") Path imageStorageDir,
                             @Value("${server.port}") int port,
                             BreedsRepository breedsRepository, AnimalsRepository animalsRepository) {
        this.imageStorageDir = imageStorageDir;
        this.port = port;
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

/*    private MetaDataPhoto initMetaDataPhoto(WebAnimal webAnimal) {
        var imageFile = webAnimal.getPhoto();

        return MetaDataPhoto.builder()
                .filepath("empty")
                .filesize(imageFile.getSize())
                .metatype(imageFile.getContentType())
                .file("empty")
                .build();
    }*/

 /*   private Animals initAnimals(WebAnimal webAnimal) {
        return Animals.builder()
                .breedId(webAnimal.getBreedId())
                .status(true)
                .nickname(webAnimal.getNickname())
                .limitations(webAnimal.getLimitations())
                .build();
    }*/

    @Override
    public ValueFromMethod addAnimal(WebAnimal webAnimal) {

        WebDTO webDTO = FileWebAPI.preparationAnimalData(this, webAnimal);
        if (!webDTO.isResult()) {
            return new ValueFromMethod(webDTO.getMessage());
        }

        webDTO.setSavedAnimal(animalsRepository.save(webDTO.getAnimals()));

        FileWebAPI.preparationAnimalData(this, webDTO);
        if (!webDTO.isResult()) {
            return new ValueFromMethod(webDTO.getMessage());
        }

        var imageFile = webAnimal.getPhoto();
        try (InputStream in = imageFile.getInputStream()) {
            Animals animalSaved = webDTO.getSavedAnimal();
            animalSaved.setMetaDataPhoto(webDTO.getMetaDataPhoto());

            animalsRepository.save(animalSaved);
            Path targetPath = Paths.get(webDTO.getSavedAnimal().getMetaDataPhoto().getFilepath());
            OutputStream out = Files.newOutputStream(targetPath, StandardOpenOption.CREATE);
            in.transferTo(out);

        } catch (IOException e) {
            return new ValueFromMethod("Internal error");
        }

        return new ValueFromMethod(true, webDTO.getSavedAnimal().getMetaDataPhoto().getFile());
    }

    @Override
    public BreedsRepository getBreedsRepository() {
        return breedsRepository;
    }

    @Override
    public Path getImageStorageDir() {
        return imageStorageDir;
    }

    @Override
    public int getPort() {
        return port;
    }
}
