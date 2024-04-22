package ru.animals.repository;

import lombok.extern.log4j.Log4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.animals.entities.UserBot;
import ru.animals.session.SessionServiceImpl;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j
public class UserBotRepositoryTest {

    @Autowired
    private UserBotRepository userBotRepository;

    @Autowired
    private SessionServiceImpl sessionService;

    @Test
    public void testRepository() {

        var userBot = UserBot.builder()
                .chatId(21L)
                .firstName("firstName")
                .lastName("lastName")
                .build();

        var repo = sessionService.getUserBotRepository();

        UserBot resSave = (UserBot) repo.save(userBot);

        assertNotNull(resSave);
        assertEquals(21L, resSave.getChatId());

    }

}
