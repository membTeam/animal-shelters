package ru.animals.utils.filePhoto;

import ru.animals.entities.Adoption;
import ru.animals.entities.ContentReport;
import ru.animals.entities.UserBot;
import ru.animals.entities.commonModel.MetaDataPhoto;
import ru.animals.entities.enumEntity.EnumStatusReport;
import ru.animals.entities.enumEntity.EnumTypeAnimation;
import ru.animals.repository.AdoptionalRepository;
import ru.animals.repository.ReportsRepository;
import ru.animals.repository.UserBotRepository;
import ru.animals.session.SessionService;
import ru.animals.session.StateReportServ;
import ru.animals.session.stateImpl.DataBufferReport;
import ru.animals.session.stateImpl.DataBufferReportDTO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

public class FilePhotoAPI {

    public static void preparationLsContent(List<DataBufferReportDTO> lsContent) {
        lsContent.addAll(List.of(
                new DataBufferReportDTO(
                        "",
                        """
                                Для регистрации отчета Вам необходимо ввести следующие данные о животном:
                                - рацион животного
                                - общее самочувствие
                                - изменение в поведении
                                - фото животного на текущий момент
                                на любом этапе регистрацию можно отменить /cancel
                                -------------------------
                                Опишите рацион животного
                                """,
                        "startRegister"),
                new DataBufferReportDTO(
                        "animalDiet",
                        "Опишите рацион питания отказ от обработки /cancel",
                        "generalMethod"),
                new DataBufferReportDTO(
                        "generalWellBeing",
                        "Опишите общее самочувствие животного отказ от обработки /cancel",
                        "generalMethod"),
                new DataBufferReportDTO(
                        "changeInBehavior",
                        "Опишите изменение в поведении отказ от обработки /cancel",
                        "generalMethod"),
                new DataBufferReportDTO(
                        "metaDataPhoto",
                        "Вставьте фото животного отказ от обработки /cancel",
                        "photoAnimal")
        ));

    }

    public static void preparationContentRepository(SessionService sessionService, FilePhotoDTO filePhotoDTO ) {
        final String imageStoragDirReport = sessionService.getImageStorageDirReport();
        final ReportsRepository reportRepo = sessionService.getReportsRepository();
        final Long chatId = filePhotoDTO.getUserBot().getChatId();
        final ContentReport savedReport = filePhotoDTO.getContentReport();
        final String fileExt = "jpg";
        final String dirRep = "report-" + chatId;

        var typeAnimation = sessionService.getBreedsRepository()
                .getTypeAnimationFromReport(Math.toIntExact(filePhotoDTO.getUserBot().getId()));
        var strTypeAnimation = EnumTypeAnimation.getStringTypeAnimation(typeAnimation).toLowerCase();

        var pathRootDirReport = Path.of(imageStoragDirReport, dirRep, strTypeAnimation);

        if (!Files.exists(pathRootDirReport)) {
            try {
                Files.createDirectories(pathRootDirReport);
            } catch (IOException ex) {
                reportRepo.deleteById(savedReport.getId());
                filePhotoDTO.setError("error creating directory");
                return;
            }
        }

        var hashCode = String.format("%d%d",
                savedReport.getId(),
                filePhotoDTO.getAdoptional().getId()).hashCode();

        var strInfo = hashCode < 0
                ? "rep-" + strTypeAnimation + "-img-" + hashCode
                : "rep-" + strTypeAnimation + "-" + hashCode;

        var strFileDistination = String.format("%s.%s", strInfo, fileExt);
        var url = String.format("/web-animal/report/%s",strInfo);
        var strDirectoryPath = Path.of(pathRootDirReport.toString(), strFileDistination).toString();

        MetaDataPhoto metaDataPhoto = MetaDataPhoto.builder()
                .file(strFileDistination)
                .filepath(strDirectoryPath)
                .metatype("image/jpeg")
                .otherinf(strInfo)
                .url(url)
                .hashcode(hashCode)
                .build();

        savedReport.setHashmetadata(strInfo);
        savedReport.setMetaDataPhoto(metaDataPhoto);
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
                .build();

        result.setAdoptional(lsContentReport.get(0));
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
