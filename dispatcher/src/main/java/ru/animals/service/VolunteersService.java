package ru.animals.service;

import ru.animals.entities.Breeds;
import ru.animals.entities.Volunteers;

import java.util.Collection;

public interface VolunteersService {
    String contactsVoluteers ();
    Collection<Volunteers> allDataBreeds();

}
