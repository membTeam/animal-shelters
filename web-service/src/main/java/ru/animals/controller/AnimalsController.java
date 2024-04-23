package ru.animals.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.animals.entities.Breeds;
import java.io.IOException;

import ru.animals.models.WebAnimal;
import ru.animals.repository.BreedsRepository;
import ru.animals.service.AnimalService;

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

    private final AnimalService animalService;
    private final BreedsRepository breedsRepository;

    public AnimalsController(@Value("${image-storage-dir}") String imageStorageDir, AnimalService animalService, BreedsRepository breedsRepository) {
        this.animalService = animalService;
        this.breedsRepository = breedsRepository;
    }

    @PostMapping(value = "/add-image/(id)", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String addImage(@PathVariable Long id, @RequestParam MultipartFile photo) {
        var res = animalService.addPhoto(id, photo);
        return "ok";
    }

    @PostMapping(value = "/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadImage(@RequestBody WebAnimal webAnimal,
                              @RequestBody MultipartFile imageFile) {

        var res = animalService.addAnimal(webAnimal, imageFile);
        return res.MESSAGE;
    }


    @GetMapping("list-animals/{breed}")
    public ResponseEntity<Collection<Breeds>> getListAnimals(@PathVariable Long breed ) {
        return ResponseEntity.ok(animalService.getListBreeds(breed));
    }





}
