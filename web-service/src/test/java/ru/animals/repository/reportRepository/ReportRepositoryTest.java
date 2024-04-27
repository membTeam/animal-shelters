package ru.animals.repository.reportRepository;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.animals.entities.ContentReport;
import ru.animals.repository.ReportsRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ReportRepositoryTest {
    @Autowired
    private ReportsRepository reportsRepository;

    @Test
    public void getMetaDataPhoto() {
        var res = reportsRepository.findByHashmetadata("rep-dog-48719");

        assertNotNull(res);
    }

}
