package ru.animals.service;

import ru.animals.entities.Breeds;
import ru.animals.models.WebAnimal;
import ru.animals.utilsDEVL.ValueFromMethod;

import java.util.List;

public interface AnimalService {
    List<Breeds> getListBreeds (Long typeAnimationsId);

    ValueFromMethod addAnimal(WebAnimal webAnimal);

}
