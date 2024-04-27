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
import ru.animals.service.impl.AnimalServiceImpl;

import javax.annotation.PostConstruct;
import java.io.OutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;

import java.util.Collection;
import java.util.Random;

@RestController
@RequestMapping("/web-animal") //localhost:8085/web-animal/list-animals/1
public class AnimalsController {

    private final AnimalService animalService;

    public AnimalsController( AnimalService animalService) {
        this.animalService = animalService;
    }

    @PostMapping(value = "/upload-image", consumes = {"multipart/form-data"})
    public String uploadImage(@ModelAttribute WebAnimal webAnimal) {

        var res = animalService.addAnimal(webAnimal);
        return res.MESSAGE;
    }

    @GetMapping("/animals")
    public ResponseEntity<List<String>> getListAnimals() {
        var res = animalService.getListAnimals();
        return ResponseEntity.ok(res);
    }

    @GetMapping("report/{info}")
    public ResponseEntity<byte[]> dowloadPhotoReport(@PathVariable String info) {

        var res = animalService.getPhotReport(info);

        if (!res.isResult()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .headers(res.getHttpHeaders())
                .body(res.getByteData());
    }

    @GetMapping("type-animals/{breed}")
    public ResponseEntity<Collection<Breeds>> getListAnimals(@PathVariable Long breed ) {
        return ResponseEntity.ok(animalService.getListBreeds(breed));
    }

}
