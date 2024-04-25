package ru.animals.repository;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class VolunteerRepositoryTest {

    @Autowired
    private AdoptionalRepository adoptionalRepository;


    @Test
    public void findByUserIdAndAnimalsId() {
        var res = adoptionalRepository.findByUserIdAndAnimalsId(1L, 1L);

        assertFalse(res);
    }

}
