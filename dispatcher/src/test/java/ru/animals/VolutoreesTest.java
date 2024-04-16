package ru.animals;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.animals.service.serviceRepostory.VolunteersService;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = DispatcherApplication.class)
public class VolutoreesTest {

    @Autowired
    private VolunteersService volunteersService;


    @Test
    public void contactsVoluteers() {
        var result = volunteersService.contactsVoluteers();

        assertTrue(result != null);
    }

}
