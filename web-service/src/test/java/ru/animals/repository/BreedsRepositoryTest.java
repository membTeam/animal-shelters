package ru.animals.repository;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BreedsRepositoryTest {

    @Autowired
    private BreedsRepository breedsRepository;


    @Test
    public void getDataForAnimation() {
        var res = breedsRepository.getDataForAnimation(1L);

        assertTrue(res.size()>0);
    }



    @Test
    public void getTypeAnimation() {
        var resulst = breedsRepository.getTypeAnimationFromReport(30);

        assertNotNull(resulst);
    }

    @Test
    public void getListBreeds() {
        var res = breedsRepository.findAllByTypeAnimationsId(1L);

        assertTrue(res.size()>0);
    }

}
