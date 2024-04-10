package ru.animals.service.repositoryTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.animals.repository.BreedsRepository;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class BreedsRepositoryTest {

    @Autowired
    private BreedsRepository breedsRepository;


    @Test
    public void testBreedsRepository() {
        var res = breedsRepository.getBreedsFromFilter(1L);

        assertTrue(res.size()>0);

    }

}
