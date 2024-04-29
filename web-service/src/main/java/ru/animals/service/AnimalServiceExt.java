package ru.animals.service;

import ru.animals.repository.AnimalsRepository;
import ru.animals.repository.BreedsRepository;

import java.nio.file.Path;

public interface AnimalServiceExt {
    BreedsRepository getBreedsRepository();
    AnimalsRepository getAnimalRepository();
    Path getImageStorageDir();
    int getPort();
}
