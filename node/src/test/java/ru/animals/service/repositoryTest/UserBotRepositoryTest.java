package ru.animals.service.repositoryTest;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.animals.repository.UserBotRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserBotRepositoryTest {
    @Autowired
    private UserBotRepository repository;

    @Test
    public void lsUserBot() {
        var res = repository.lsUserBot();

        assertTrue(res.size() > 0);
    }

    @Test
    public void findByChatId() {
        var res = repository.findByChatId(5105101885L);

        assertTrue(res.isPresent());
    }

}
