package ru.animals.sevice;


import ru.animals.entities.Breeds;

import java.util.Collection;

public interface AnimalService {
    Collection<Breeds> getListBreeds (Long typeAnimationsId);
}
