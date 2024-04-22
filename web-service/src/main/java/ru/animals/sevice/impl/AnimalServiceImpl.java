package ru.animals.sevice.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.animals.entities.Breeds;
import ru.animals.repository.BreedsRepository;
import ru.animals.sevice.AnimalService;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class AnimalServiceImpl implements AnimalService {

    private final BreedsRepository breedsRepository;

    @Override
    public Collection<Breeds> getListBreeds(Long typeAnimationsId) {
        var res = breedsRepository.findAllByTypeAnimationsId(typeAnimationsId);
        return res;
    }
}
