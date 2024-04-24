package ru.animals.service;

import ru.animals.repository.BreedsRepository;

import java.nio.file.Path;

public interface AnimalServiceExt {
    BreedsRepository getBreedsRepository();
    Path getImageStorageDir();
    int getPort();
}
