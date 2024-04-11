package ru.animals.service.repositoryTest;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.animals.repository.CommonReposities;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CommonRepositoriesTest {

    @Autowired
    private CommonReposities commonReposities;


    @Test
    public void isExistsUserBot() {
        var result = commonReposities.isExistsUserBot(1L);

        assertFalse(result);
    }

    @Test
    public void findAllVolunteer() {
        var result = commonReposities.findAllVolunteer();

        assertTrue(result.size() > 0);
    }

    @Test
    public void findAllShelters() {
        var result = commonReposities.findAllShelters();

        assertTrue(result.size()>0);
    }

}
