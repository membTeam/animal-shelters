package ru.animals.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.animals.entities.Breeds;
import ru.animals.models.WebAnimal;
import ru.animals.repository.BreedsRepository;
import ru.animals.service.AnimalService;
import ru.animals.utilsDEVL.ValueFromMethod;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AnimalServiceImpl implements AnimalService {

    private final BreedsRepository breedsRepository;

    @Override
    public List<Breeds> getListBreeds(Long typeAnimationsId) {
        return breedsRepository.findAllByTypeAnimationsId(typeAnimationsId);
    }

    @Override
    public ValueFromMethod addAnimal(WebAnimal webAnimal, MultipartFile photo) {
        var nickname = webAnimal.getNickname();
        var fileName = photo.getOriginalFilename();

        return new ValueFromMethod(true, "ok");
    }

    private int getNumberRandom() {
        int min = 10000;
        int max = 20000;
        int diff = max - min;

        Random random = new Random();
        return random.nextInt(diff + 1) + min;
    }

}