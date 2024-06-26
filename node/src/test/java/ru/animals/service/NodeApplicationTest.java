package ru.animals.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.animals.NodeApplication;
import ru.animals.repository.ShelterRepository;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = NodeApplication.class)
public class NodeApplicationTest {

    @Autowired
    private ShelterRepository shelterRepository;

    @Test
    public void contextLoads() {

        var result = shelterRepository.getDataShelters("rules.txt");

        assertTrue(result.size()>0);
    }

}
