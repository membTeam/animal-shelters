package ru.animals.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.animals.entities.enumEntity.EnumAdoptionState;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AdoptionalRepositoryTest {

    @Autowired
    private AdoptionalRepository adoptionalRepository;

    @Test
    public void containsByUserId() {
        var res = adoptionalRepository.containsByUserId(30L);

        assertTrue(res);
    }

    @Test
    public void findByUserIdAndOnProbational() {
        var res = adoptionalRepository
                .findByUserIdAndOnProbational(30L, EnumAdoptionState.ON_PROBATION);


        assertTrue(res.size()>0);

    }

    @Test
    public void findByUserIForONPROBATION() {
        var res = adoptionalRepository.findByUserIdForONPROBATION(30L);

        assertNotNull(res);
        assertTrue(res.size() == 1);

    }

}
