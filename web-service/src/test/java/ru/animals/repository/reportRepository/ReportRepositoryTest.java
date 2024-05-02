package ru.animals.repository.reportRepository;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.animals.entities.ContentReport;
import ru.animals.entities.commonModel.WebVerificationResponseDTO;
import ru.animals.models.serviceWebModels.WebAnimalsResponseServ;
import ru.animals.repository.ReportsRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ReportRepositoryTest {
    @Autowired
    private ReportsRepository reportsRepository;

    @Test
    public void getChatId() {
        var res = reportsRepository.getChatId(20L);

        assertTrue(res > 0);
    }


    @Test
    public void getDateFinishReport() {
        LocalDateTime dateFinish = reportsRepository.getDateFinishReport(18L);

        assertNotNull(dateFinish);
    }


    @Test
    public void verificationReport() {
        var resFromRepository = reportsRepository.verificationReport();

        var resListWebVerification = WebAnimalsResponseServ.getListWebVerification(reportsRepository);

        assertTrue(resFromRepository.size()>0);

        assertTrue(resListWebVerification.size()>0);
    }


}
