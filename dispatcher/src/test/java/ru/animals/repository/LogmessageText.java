package ru.animals.repository;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LogmessageText {

    private LogmessageRepository logmessageRepository;

    @Test
    public void findAllByChatId() {
        var res = logmessageRepository.getReferenceById(30L);

    }


}
