package ru.animals.service.repositoryTest;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.animals.repository.VolunteerRepository;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class VolunteerRepositoryTest {

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Test
    public void volunteerRepositoryTest_forBreeds() {
        var res = volunteerRepository.getBreedsFromFilter(2L);

        assertTrue(res.size()>0);
    }

    @Test
    public void volunteerRepositoryTest_byName() {
        var res = volunteerRepository.getVolunteersByFilter("Климов В.П.");

        assertTrue(res.size()>0);
    }

    @Test
    public void volunteerRepositoryTest() {
        var res = volunteerRepository.findAll();

        assertTrue(res.size()>0);
    }
}
