package ru.animals.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.support.Repositories;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.animals.entities.Breeds;
import ru.animals.sevice.AnimalService;
import ru.animals.sevice.impl.AnimalServiceImpl;

import java.util.Collection;

@RestController
@RequestMapping("/web-animal") //localhost:8085/web-animal/...
@RequiredArgsConstructor
public class AnimalsController {

    private final AnimalService animalService;

    @GetMapping("list-animals/{breed}")
    public ResponseEntity<Collection<Breeds>> getListAnimals(@PathVariable Long breed ) {
        return ResponseEntity.ok(animalService.getListBreeds(breed));
    }



}
