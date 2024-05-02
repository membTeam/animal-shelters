package ru.animals.repository;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AnimalsRepositoryTest {

    @Autowired
    private AnimalsRepository animalsRepository;

    @Test
    public void findByHashmetadataNext() {
        var result = animalsRepository.findByHashmetadataNext("animal-dog-49651");

        assertNotNull(result);
    }
}
