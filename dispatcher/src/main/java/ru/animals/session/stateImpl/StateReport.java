package ru.animals.session.stateImpl;

import lombok.Getter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.animals.controller.TelegramBot;
import ru.animals.entities.ContentReport;
import ru.animals.entities.commonModel.MetaDataPhoto;
import ru.animals.session.SessionServiceImpl;
import ru.animals.session.StateReportServ;
import ru.animals.telegramComp.TelgramComp;
import ru.animals.utils.filePhoto.FilePhotoAPI;
import ru.animals.utils.filePhoto.FilePhotoDTO;
import ru.animals.entities.enumEntity.EnumTypeAnimation;


public class StateReport extends BaseState implements StateReportServ {

    private Long chatId;

    @Getter
    private LocalDateTime dateTimeLastAppeal;
    @Getter
    private boolean resultEnd = false;
    @Getter
    private boolean startState = false;


    private DataBufferReport dataBufferReport;

    private LinkedList<DataBufferReportDTO> lsState;

    public StateReport(Long chatId) {
        this.chatId = chatId;
        this.dateTimeLastAppeal = LocalDateTime.now();
        this.dataBufferReport = new DataBufferReport();
        this.dataBufferReport.setChatId(chatId);

        // TODO: только для отладки процедуры загрузки фотографии
        this.dataBufferReport.setChangeInBehavior("ChangeInBehavior");
        this.dataBufferReport.setAnimalDiet("AnimalDie");
        this.dataBufferReport.setGeneralWellBeing("GeneralWellBeing");

        lsState = new LinkedList<>();
        lsState.addAll(List.of(
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

    private SendMessage startRegister(SessionServiceImpl sessionService, Update update) {
        DataBufferReportDTO reportDTO = lsState.getFirst();
        lsState.removeFirst();

        return TelgramComp.defaultSendMessage(chatId, reportDTO.getTextMessage());
    }

    private SendMessage generalMethod(SessionServiceImpl sessionService, Update update) {
        DataBufferReportDTO reportDTO = lsState.getFirst();

        var textFromUpdate = update.getMessage().getText();
        String textMessage = null;

        if (reportDTO.getStrProperty().equals("animalDiet")) {
            dataBufferReport.setAnimalDiet(textFromUpdate);
        } else if (reportDTO.getStrProperty().equals("generalWellBeing")) {
            dataBufferReport.setGeneralWellBeing(textFromUpdate);
        } else if (reportDTO.getStrProperty().equals("changeInBehavior")) {
            dataBufferReport.setChangeInBehavior(textFromUpdate);
        } else {
            textMessage = "The processing method was not found";
        }

        if (textMessage == null) {
            lsState.removeFirst();
            textMessage = lsState.getFirst().getTextMessage();
        }

        return TelgramComp.defaultSendMessage(chatId, textMessage);
    }

    private SendMessage photoAnimal(SessionServiceImpl sessionService, Update update) {
        DataBufferReportDTO reportDTO = lsState.getFirst();
        var reportRepo = sessionService.getReportsRepository();

        FilePhotoDTO filePhotoDTO = FilePhotoAPI.preparationContentRepository(sessionService, this);
        if (!filePhotoDTO.isResult()) {
            return TelgramComp.defaultSendMessage(chatId,"Data preparation error");
        }

        ContentReport savedReport = null;
        try {
            savedReport = reportRepo.save(filePhotoDTO.getContentReport());
            filePhotoDTO.setContentReport(savedReport);
        } catch (Exception ex) {
            return TelgramComp.defaultSendMessage(chatId, "Error save data");
        }

        FilePhotoAPI.preparationContentRepository(sessionService, filePhotoDTO);
        if (!filePhotoDTO.isResult()) {
            return TelgramComp.defaultSendMessage(chatId,"File system error");
        }

        /*String imageStoragDirReport = sessionService.getImageStorageDirReport();
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
                return TelgramComp.defaultSendMessage(chatId,"error creating directory");
            }
        }

        var strDirectoryPath =
                Path.of(pathRootDirReport.toString(), strFileDistination).toString();*/

        var strFileDistination  = filePhotoDTO.getStrFileDistination();
        var strTypeAnimation = filePhotoDTO.getStrTypeAnimation();
        var strDirectoryPath = filePhotoDTO.getStrDirectoryPath();

        long fileSize = 0;
        try {
            File file = new java.io.File(strDirectoryPath);
            TelegramBot telegramBot = sessionService.getTelegramBot();

            var resFile = telegramBot.downloadFile(update, file);
            fileSize = resFile.getFileSize();

        } catch (TelegramApiException ex) {
            reportRepo.deleteById(savedReport.getId());
            return TelgramComp.defaultSendMessage(chatId, "Error download photo");
        }

        int port = sessionService.getWebServerPort();
        var info = strFileDistination.substring(0, strFileDistination.indexOf("."));
        String url = String.format("localhost:%d/report/%s/%s",port,strTypeAnimation, info);

        MetaDataPhoto metaDataPhoto = MetaDataPhoto.builder()
                .file(strFileDistination)
                .filepath(strDirectoryPath)
                .metatype("image/jpeg")
                .filesize(fileSize)
                .otherinf(info)
                .url(url)
                .hashcode(0)
                .build();

        savedReport.setMetaDataPhoto(metaDataPhoto);
        try {
            reportRepo.save(savedReport);
        } catch (Exception ex) {
            try {
                reportRepo.deleteById(savedReport.getId());
                Files.deleteIfExists(Paths.get(strDirectoryPath));
            } catch(Exception ignored) { }

            return TelgramComp.defaultSendMessage(chatId,"The server rejected the processing");
        }

        resultEnd = true;
        return TelgramComp.defaultSendMessage(chatId, "Отчет принят на проверку");
    }

    @Override
    public SendMessage getSendMessage(SessionServiceImpl sessionService, Update update) {
        if (lsState.size() > 0) {
            DataBufferReportDTO reportDTO = lsState.getFirst();

            return switch (lsState.getFirst().getStrMethod()) {
                case "startRegister" -> startRegister(sessionService, update);
                case "generalMethod" -> generalMethod(sessionService, update);
                case "photoAnimal" -> photoAnimal(sessionService, update);

                default -> TelgramComp.defaultSendMessage(chatId, "Метод не найден");
            };
        } else {
            return TelgramComp.defaultSendMessage(chatId, "Отчет по животным сдан");
        }
    }

    @Override
    public ResultState getResultState() {
        return ResultState.builder()
                .START_STATE(startState)
                .RESULT_END(resultEnd)
                .CHAT_ID(chatId)
                .LAST_APPEAL(dateTimeLastAppeal)
                .build();
    }

    @Override
    public DataBufferReport getDataBufferReport() {
        return dataBufferReport;
    }
}
