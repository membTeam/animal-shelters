package ru.animals.repository.breedsRepository;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.animals.repository.BreedsRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BreedsRepositoryTest {

    @Autowired
    private BreedsRepository breedsRepository;

    @Test
    public void getPhotoForAnimation() {
        var res = breedsRepository.getPhotoForAnimation(1L);

        assertTrue(res.size()>0);
    }

}
