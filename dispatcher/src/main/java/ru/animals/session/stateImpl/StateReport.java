package ru.animals.session.stateImpl;

import lombok.Getter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.animals.entities.Adoption;
import ru.animals.entities.ContentReport;
import ru.animals.entities.enumEntity.EnumStatusReport;
import ru.animals.session.SessionServiceImpl;
import ru.animals.telegramComp.TelgramComp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class StateReport extends BaseState {

    private Long chatId;

    @Getter
    private LocalDateTime dateTimeLastAppeal;
    @Getter
    private boolean resultEnd = false;
    @Getter
    private boolean startState = false;


    private DataBufferReport dataBufferReport;

    private LinkedList<DataBufferReportDTO> lsState;

    public StateReport(long chatId) {
        this.chatId = chatId;
        this.dateTimeLastAppeal = LocalDateTime.now();
        this.dataBufferReport = new DataBufferReport();

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

        var text = update.getMessage().getText();
        String textMessage = null;

        if (reportDTO.getStrProperty().equals("animalDiet")) {
            dataBufferReport.setAnimalDiet(text);
        } else if (reportDTO.getStrProperty().equals("generalWellBeing")) {
            dataBufferReport.setGeneralWellBeing(text);
        } else if (reportDTO.getStrProperty().equals("changeInBehavior")) {
            dataBufferReport.setChangeInBehavior(text);
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

        var sendMessage = update.getMessage();
        var chatId = sendMessage.getChatId();

        var imageStorageDirReport = sessionService.getImageStorageDirReport();

        var userbotRepo = sessionService.getUserBotRepository();
        var reportRepo = sessionService.getReportsRepository();
        var adoptionalRepo = sessionService.getAdoptionalRepository();

        var optionalUserBot = userbotRepo.findByChatId(chatId);
        if (optionalUserBot.isEmpty()) {
            return TelgramComp.defaultSendMessage(chatId,"Пользователь не найден");
        }

        var userBot = optionalUserBot.get();

        if (!adoptionalRepo.containsByUserId(userBot.getId())) {
            return TelgramComp.defaultSendMessage(chatId,"Нет данных по усыновлению");
        }

        Adoption adoption = adoptionalRepo.findByUserIdForONPROBATION(userBot.getId()).get(0);

        ContentReport contentReport = ContentReport.builder()
                .date(LocalDate.now())
                .adoption_id(adoption.getId())
                .statusReport(EnumStatusReport.RECEIPT_REPORT)
                .generalWellBeing(dataBufferReport.getGeneralWellBeing())
                .changeBehavior(dataBufferReport.getChangeInBehavior())
                .animalDiet(dataBufferReport.getAnimalDiet())
                .metaDataPhoto(null)
                .build();

        ContentReport savedReport = null;
        try {
            savedReport = reportRepo.save(contentReport);
        } catch (Exception ex) {
            return TelgramComp.defaultSendMessage(chatId, "Error save data");
        }

        return null;
    }

    @Override
    public SendMessage getSendMessage(SessionServiceImpl sessionService, Update update) {
        if (lsState.size() > 0) {
            DataBufferReportDTO reportDTO = lsState.getFirst();

            return switch (lsState.getFirst().getStrMethod()) {
                case "startRegister" -> startRegister(sessionService, update);
                case "GeneralWellBeing" -> generalMethod(sessionService, update);
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

}
