package ru.animals.repository;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ReportRepositoryTest {
    @Autowired
    private ReportsRepository reportsRepository;

    @Test
    public void findByHashmetadata() {
        var resData = reportsRepository.findByHashmetadata("rep-dog-48906");

        assertNotNull(resData);
    }
}
