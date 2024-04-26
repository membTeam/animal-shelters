package ru.animals.reportUser;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.animals.entities.enumEntity.EnumTypeAnimation;
import ru.animals.session.SessionService;
import ru.animals.session.SessionServiceImpl;
import ru.animals.telegramComp.TelgramComp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CreateDirectoryForReportTest {

    @Autowired
    private SessionServiceImpl sessionService;

    @Test
    public void createDirectoryForReport() throws IOException {

        var imageStoragDirReport = sessionService.getImageStorageDirReport();

        var typeAnimation = sessionService.getBreedsRepository()
                .getTypeAnimationFromReport(Math.toIntExact(30L));
        var strTypeAnimation = EnumTypeAnimation.getStringTypeAnimation(typeAnimation).toLowerCase();

        var chatId = 5105101885L;
        var dirRep = "report-" + chatId;
        Path pathRootDirReport = Path.of(imageStoragDirReport, dirRep, strTypeAnimation);

        if (!Files.exists(pathRootDirReport)) {
            Files.createDirectories(pathRootDirReport);
            assertTrue(Files.exists(pathRootDirReport));
        }


    }

}
