package ru.animals.models;

import ru.animals.service.AnimalServiceExt;

import java.util.Optional;

public class FileWebAPI {
    public static  preparationAnimalData(AnimalServiceExt animalServiceExt, WebAnimal webAnimal) {

    }

    private static Optional<String> getFileExtension(String fileName) {
        final int indexOfLastDot = fileName.lastIndexOf('.');

        if (indexOfLastDot == -1) {
            return Optional.empty();
        } else {
            return Optional.of(fileName.substring(indexOfLastDot + 1));
        }
    }
}
