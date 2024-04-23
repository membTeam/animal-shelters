package ru.animals.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.animals.entities.Breeds;
import ru.animals.entities.Shelters;
import ru.animals.entities.UserBot;
import ru.animals.repository.ShelterRepository;
import ru.animals.repository.UserBotRepository;
import ru.animals.service.AnimalService;

import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/web-animal")
//localhost:8085/web-animal/list-animals/1
// localhost:8085/web-animal/shelter
public class AnimalsController {

    private final AnimalService animalService;
    private final ShelterRepository shelterRepository;

    private final UserBotRepository userBotRepository;

    @GetMapping("shelter")
    public List<Shelters> getShelter() {
        return shelterRepository.findAll();
    }

    @GetMapping("list-animals/{breed}")
    public List<Breeds> getListAnimals(@PathVariable Long breed ) {
        return animalService.getListBreeds(breed);
    }
}
