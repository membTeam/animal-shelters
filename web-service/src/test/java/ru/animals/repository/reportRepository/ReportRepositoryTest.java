package ru.animals.repository.reportRepository;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.animals.entities.ContentReport;
import ru.animals.entities.commonModel.WebVerificationResponseDTO;
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
    public void getDateFinishReport() {
        LocalDateTime dateFinish = reportsRepository.getDateFinishReport(18L);

        assertNotNull(dateFinish);
    }


    @Test
    public void verificationReport() {
        var res = reportsRepository.verificationReport();

        List<WebVerificationResponseDTO> lsData = new ArrayList<>();
        res.forEach(str-> lsData.add(new WebVerificationResponseDTO(str)) );

        assertTrue(res.size()>0);

        assertTrue(lsData.size()>0);
    }


}
