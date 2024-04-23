package ru.animals.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.animals.entities.Breeds;
import java.io.IOException;

import ru.animals.repository.BreedsRepository;
import ru.animals.sevice.AnimalService;

import javax.annotation.PostConstruct;
import java.io.OutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

import java.util.Collection;
import java.util.Random;

@RestController
@RequestMapping("/web-animal") //localhost:8085/web-animal/list-animals/1
public class AnimalsController {

    private final Path imageStorageDir;

    private final AnimalService animalService;
    private final BreedsRepository breedsRepository;

    public AnimalsController(@Value("${image-storage-dir}") Path imageStorageDir, AnimalService animalService, BreedsRepository breedsRepository) {
        this.imageStorageDir = imageStorageDir;
        this.animalService = animalService;
        this.breedsRepository = breedsRepository;
    }

    @PostConstruct
    public void ensureDirectoryExists() throws IOException  {
        if (!Files.exists(this.imageStorageDir)) {
            Files.createDirectories(this.imageStorageDir);
        }
    }

    @PostMapping(value = "/upload-image/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadImage(@PathVariable Long id,
                              @RequestParam MultipartFile imageFile) throws IOException {
        final String fileExtension = Optional.ofNullable(imageFile.getOriginalFilename())
                .flatMap(AnimalsController::getFileExtension)
                .orElse("");

        var optBreed = breedsRepository.findById(id);
        if (optBreed.isEmpty()) {
            return "internal erro";
        }

        var breed = optBreed.get();

        final String strBreed = breed.getTypeAnimationsId() == 1 ? "dog" : "cat";


        int randNumber = getRandNumber();

        final String targetFileName = String.format("%s%d-%d.%s", strBreed, id, randNumber, fileExtension);

        final Path targetPath = this.imageStorageDir.resolve(targetFileName);

        try (InputStream in = imageFile.getInputStream()) {
            try (OutputStream out = Files.newOutputStream(targetPath, StandardOpenOption.CREATE)) {
                in.transferTo(out);
            }
        }

        return targetFileName;
    }


    @GetMapping("list-animals/{breed}")
    public ResponseEntity<Collection<Breeds>> getListAnimals(@PathVariable Long breed ) {
        return ResponseEntity.ok(animalService.getListBreeds(breed));
    }

    private static Optional<String> getFileExtension(String fileName) {
        final int indexOfLastDot = fileName.lastIndexOf('.');

        if (indexOfLastDot == -1) {
            return Optional.empty();
        } else {
            return Optional.of(fileName.substring(indexOfLastDot + 1));
        }
    }

    private static int getRandNumber() {
        int min = 10000;
        int max = 20000;
        int diff = max - min;

        Random random = new Random();
        return random.nextInt(diff + 1) + min;
    }

}
