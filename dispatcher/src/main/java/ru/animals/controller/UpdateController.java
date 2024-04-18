package ru.animals.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.animals.collbackCommand.DistrCollbackCommandImpl;
import ru.animals.service.serviceRepostory.CommonService;
import ru.animals.service.serviceRepostory.ServUserBot;
import ru.animals.service.serviceRepostory.UpdateProducer;
import ru.animals.utils.DevlAPI;
import ru.animals.utils.UtilsMessage;
import ru.animals.utils.UtilsSendMessage;
import ru.animals.utilsDEVL.FileAPI;
import ru.animals.utilsDEVL.ValueFromMethod;
import ru.animals.utilsDEVL.entitiesenum.EnumTypeParamCollback;


@Component
@Log4j
public class UpdateController {
    private TelegramBot telegramBot;
    private UtilsMessage utilsMessage;
    private UpdateProducer updateProducer;
    private UtilsSendMessage utilsSendMessage;
    private CommonService commonService;
    private DistrCollbackCommandImpl commonCollbackService;
    private ServUserBot servUserBot;


    public UpdateController(UtilsMessage utilsMessage,
                            UpdateProducer updateProducer,
                            UtilsSendMessage utilsSendMessage,
                            CommonService commonService,
                            DistrCollbackCommandImpl commonCollbackService, ServUserBot servUserBot

    ) {
        this.utilsMessage = utilsMessage;
        this.updateProducer = updateProducer;
        this.utilsSendMessage = utilsSendMessage;
        this.commonService = commonService;
        this.commonCollbackService = commonCollbackService;
        this.servUserBot = servUserBot;
    }

    public void registerBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    /**
     * Убирает начальный /
     * @param text
     * @return
     */
    private String modifingTextMessage(String text) {
        return text.charAt(0) == '/' ? text.substring(1) : text;
    }

    private Long getCharIdFromUpdate(Update update) throws Exception {

        ValueFromMethod<Long> res = DevlAPI.getChatIdFromUpdate(update);

        if (!res.RESULT) {
            throw new Exception(res.MESSAGE);
        }

        return res.getValue();
    }

    private String getTextMessFromUpdate(Update update) {
        return DevlAPI.getTextMessFromUpdate(update);
    }


    /**
     * Менеджер команд в зависимости от типа сообщения
     * @param update
     */
    public void distributeMessages(Update update) {

        if (update == null) {
            return;
        }

        try {
            if (utilsSendMessage.isERROR()) {
                log.error(utilsSendMessage.getMessageErr());
                throw new Exception("Internal error");
            }

            switch (DevlAPI.typeUpdate(update, false)) {
                case TEXT_MESSAGE -> distributeMessagesBytype(update);
                case COLLBACK -> distributeCallbackQueryMessages(update);
                default -> {
                    throw new IllegalArgumentException("Тип не определен");
                }
            }

        } catch (Exception e) {
            try {
                var chartId = getCharIdFromUpdate(update);

                var sendMessage = utilsMessage.generateSendMessageWithText(chartId, e.getMessage());
                log.error(sendMessage);

                telegramBot.sendAnswerMessage(sendMessage);

            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
        }
    }

    /**
     * Менеджер текстовых сообщений
     * @param update
     * @throws Exception
     */
    private void distributeMessagesBytype(Update update) throws Exception {

        String textMess = getTextMessFromUpdate(update);
        Long charId = getCharIdFromUpdate(update);

        var structureCommand = utilsSendMessage.getStructureCommand(textMess);
        switch (structureCommand.getEnumTypeMessage()) {
            case BTMMENU -> distributeMenu(charId, textMess);
            case TEXT_MESSAGE -> sendTextMessage(charId, textMess);
            case SELMENU -> distributeParse(charId);
            default -> {
                throw new IllegalArgumentException("");
            }
        };
    }

    /**
     * обработка команды /start в зависимости от статуса пользователя
     * @param charId
     * @throws Exception
     */
    private void distributeParse(Long charId) throws Exception {
        var statusUser = servUserBot.statudUserBot(charId);
        var btnMenuStart = switch (statusUser) {
            case USER_NOT_REGISTER -> "register";
            case NO_PROBATION_PERIOD -> "noprobation";
            case ON_PROBATION_PERIOD -> "onprobation";
            default -> "empty";
        };

        if (statusUser.equals("empty")) {
            throw new Exception("the command was not found");
        }

        distributeMenu(charId, btnMenuStart);
    }

    private void distributeMenu(Long charId, String textMess) throws Exception {
        var structureCommand = utilsSendMessage.getStructureCommand(textMess);

        var sendMessage = utilsMessage.generateSendMessageWithBtn(charId, structureCommand);

        telegramBot.sendAnswerMessage(sendMessage);

    }

    /**
     * Обработка collback команд
     * @param update
     */
    private void distributeCallbackQueryMessages(Update update) throws Exception {
        var textQuery = update.getCallbackQuery().getData();
        var chartId = getCharIdFromUpdate(update);

        var structCollbackCommand = utilsSendMessage.getStructCommandCollback(textQuery);

        var enumType = structCollbackCommand.getEnumTypeParameter();
        if (enumType == EnumTypeParamCollback.TCL_TXT) {
            sendTextMessage(chartId,
                    utilsSendMessage.getStructureCommand(structCollbackCommand).getSource());

        } else if (enumType == EnumTypeParamCollback.TCL_BTN) {
            distributeMenu(chartId,
                    utilsSendMessage.getStructureCommand(structCollbackCommand).getSource());

        } else if (enumType == EnumTypeParamCollback.TCL_DBD) {

            // TODO: исправить идентификатор метода
            var sendMessage = commonCollbackService.distributeStrCommand(chartId, structCollbackCommand);

            telegramBot.sendAnswerMessage(sendMessage);

        } else {
            distributeMenu(chartId, textQuery);
        }
    }


    private void sendTextMessage(Long charId,
                                 String fileSource) throws Exception {

        ValueFromMethod<String> dataFromFile = FileAPI.readDataFromFile(fileSource);

        if (!dataFromFile.RESULT) {
            throw new Exception("Контент не найден");
        }

        var txtMessage = dataFromFile.getValue();

        var sendMessage = utilsMessage.generateSendMessageWithText(charId, txtMessage);

        sendMessage.setParseMode(ParseMode.MARKDOWN);
        telegramBot.sendAnswerMessage(sendMessage);
    }

    public void setView(SendMessage sendMessage) {
        telegramBot.sendAnswerMessage(sendMessage);
    }

}

