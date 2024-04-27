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

        lsState = new LinkedList<>();
        FilePhotoAPI.preparationLsContent(lsState);

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

        var resSetting = dataBufferReport.setProperty(reportDTO, textFromUpdate);

        if (resSetting) {
            lsState.removeFirst();
            textMessage = lsState.getFirst().getTextMessage();
        } else {
            textMessage = "Error processing the message string";
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

        ContentReport savedReport = filePhotoDTO.getContentReport();
        try {
            savedReport = reportRepo.save(savedReport);
            filePhotoDTO.setContentReport(savedReport);
        } catch (Exception ex) {
            return TelgramComp.defaultSendMessage(chatId, "Error save data");
        }

        FilePhotoAPI.preparationContentRepository(sessionService, filePhotoDTO);
        if (!filePhotoDTO.isResult()) {
            return TelgramComp.defaultSendMessage(chatId,"File system error");
        }

        var strDirectoryPath = filePhotoDTO.getStrDirectoryPath();
        try {
            File file = new java.io.File(strDirectoryPath);
            TelegramBot telegramBot = sessionService.getTelegramBot();

            var resFile = telegramBot.downloadFile(update, file);
            var fileSize = resFile.getFileSize();

            savedReport.getMetaDataPhoto().setFilesize(fileSize);

        } catch (TelegramApiException ex) {
            reportRepo.deleteById(savedReport.getId());
            return TelgramComp.defaultSendMessage(chatId, "Error download photo");
        }

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
