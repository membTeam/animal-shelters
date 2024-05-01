package ru.animals.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.animals.entities.Breeds;
import ru.animals.models.WebAnimal;
import ru.animals.models.WebAnimalResponse;
import ru.animals.service.AnimalService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/web-animal")
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

    @GetMapping("/view-animal/{info}")
    public ResponseEntity<byte[]> downloadPhotoAnimation(@PathVariable String info) {
        var res = animalService.getPhotoAnimal(info);
        if (!res.isResult()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .headers(res.getHttpHeaders())
                .body(res.getByteData());
    }

    @GetMapping("/list-animals/{id}")
    public ResponseEntity<List<WebAnimalResponse>> getListAnimals(@PathVariable Long id) {
        var res = animalService.getListAnimals(id);
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
    public ResponseEntity<Collection<Breeds>> getListBreeds(@PathVariable Long breed ) {
        return ResponseEntity.ok(animalService.getListBreeds(breed));
    }

}
