package ru.animals.service;

import org.springframework.web.multipart.MultipartFile;
import ru.animals.entities.Breeds;
import ru.animals.models.WebAnimal;
import ru.animals.utilsDEVL.ValueFromMethod;

import java.util.Collection;

public interface AnimalService {
    Collection<Breeds> getListBreeds (Long typeAnimationsId);

    ValueFromMethod addAnimal(WebAnimal webAnimal, MultipartFile photo);
}
