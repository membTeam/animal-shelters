package ru.animals.utils.filePhoto;

import ru.animals.entities.Adoption;
import ru.animals.entities.ContentReport;
import ru.animals.entities.UserBot;
import ru.animals.entities.enumEntity.EnumStatusReport;
import ru.animals.entities.enumEntity.EnumTypeAnimation;
import ru.animals.repository.AdoptionalRepository;
import ru.animals.repository.UserBotRepository;
import ru.animals.session.SessionService;
import ru.animals.session.StateReportServ;
import ru.animals.session.stateImpl.DataBufferReport;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

public class FilePhotoAPI {

    public static void preparationContentRepository(SessionService sessionService, FilePhotoDTO filePhotoDTO ) {
        String imageStoragDirReport = sessionService.getImageStorageDirReport();
        var reportRepo = sessionService.getReportsRepository();

        var chatId = filePhotoDTO.getUserBot().getChatId();
        var savedReport = filePhotoDTO.getContentReport();

        var fileExt = "jpg";
        var strFileDistination = String.format("rep-%d-%d.%s", chatId, savedReport.getId(),  fileExt);

        var typeAnimation = sessionService.getBreedsRepository()
                .getTypeAnimationFromReport(Math.toIntExact(filePhotoDTO.getUserBot().getId()));
        var strTypeAnimation = EnumTypeAnimation.getStringTypeAnimation(typeAnimation).toLowerCase();

        var dirRep = "report-" + chatId;
        var pathRootDirReport = Path.of(imageStoragDirReport, dirRep, strTypeAnimation);

        if (!Files.exists(pathRootDirReport)) {
            try {
                Files.createDirectories(pathRootDirReport);
            } catch (IOException ex) {
                reportRepo.deleteById(savedReport.getId());
                filePhotoDTO.setError("error creating directory");
            }
        }

        var strDirectoryPath = Path.of(pathRootDirReport.toString(), strFileDistination).toString();

        filePhotoDTO.setStrTypeAnimation(strTypeAnimation);
        filePhotoDTO.setStrFileDistination(strFileDistination);
        filePhotoDTO.setStrDirectoryPath(strDirectoryPath);
    }

    public static FilePhotoDTO preparationContentRepository(SessionService sessionService, StateReportServ reportServ ) {
        FilePhotoDTO result = FilePhotoDTO.getFilePhotoDTO();

        DataBufferReport buferReport = reportServ.getDataBufferReport();
        UserBotRepository userRepository = sessionService.getUserBotRepository();
        AdoptionalRepository adoptionalRepo = sessionService.getAdoptionalRepository();

        var optUserBot = userRepository.findByChatId(buferReport.getChatId());
        if (optUserBot.isEmpty()) {
            return initWithError("Пользователь не найден");
        }

        UserBot userBot = optUserBot.get();

        List<Adoption> lsContentReport = adoptionalRepo
                .findByUserIdForONPROBATION(userBot.getId());
        if (lsContentReport.size() == 0) {
            return initWithError("Нет данных по усыновлению");
        }

        ContentReport contentReport = ContentReport.builder()
                .date(LocalDate.now())
                .adoptionId(lsContentReport.get(0).getId())
                .statusReport(EnumStatusReport.RECEIPT_REPORT)
                .changeBehavior(buferReport.getChangeInBehavior())
                .animalDiet(buferReport.getAnimalDiet())
                .generalWellBeing(buferReport.getGeneralWellBeing())
                .statusReport(EnumStatusReport.RECEIPT_REPORT)
                .metaDataPhoto(null)
                .build();

        result.setContentReport(contentReport);
        result.setUserBot(userBot);

        return result;
    }

    private static FilePhotoDTO initWithError(String mes) {
        var result = new FilePhotoDTO();
        result.setError(mes);

        return result;
    }

}
