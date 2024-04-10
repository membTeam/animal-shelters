package ru.animals.service.repositoryTest;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.animals.repository.AnimalsRepository;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class AnimalsRepositoryTest {
    @Autowired
    private AnimalsRepository animalsRepository;



    @Test
    public void animalRepositoryTest() {
        var res = animalsRepository.findAll();

        assertTrue(res.size() == 0);
    }



}
