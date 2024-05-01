package ru.animals.service;

import org.springframework.http.ResponseEntity;
import ru.animals.entities.Breeds;
import ru.animals.models.WebAnimal;
import ru.animals.models.WebAnimalResponse;
import ru.animals.models.WebResultData;
import ru.animals.utilsDEVL.ValueFromMethod;

import java.util.List;

public interface AnimalService {
    List<Breeds> getListBreeds (Long typeAnimationsId);

    ValueFromMethod addAnimal(WebAnimal webAnimal);

    WebResultData getPhotReport(String info);

    WebResultData getPhotoAnimal(String info);

    List<WebAnimalResponse> getListAnimals(Long id);
}
