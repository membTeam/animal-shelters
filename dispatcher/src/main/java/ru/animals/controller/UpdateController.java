package ru.animals.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.animals.service.CommonService;
import ru.animals.service.UpdateProducer;

//import ru.animals.service.VolunteersService;

import ru.animals.utils.UtilsMessage;
import ru.animals.utils.UtilsSendMessage;
import ru.animals.utilsDEVL.DataFromParser;
import ru.animals.utilsDEVL.FileAPI;
import ru.animals.utilsDEVL.entitiesenum.EnumTypeMessage;

@Component
@Log4j
public class UpdateController {
    private TelegramBot telegramBot;
    private UtilsMessage utilsMessage;
    private UpdateProducer updateProducer;
    private UtilsSendMessage utilsSendMessage;
    private CommonService commonService;

    public UpdateController(UtilsMessage utilsMessage,
                            UpdateProducer updateProducer,
                            UtilsSendMessage utilsSendMessage, CommonService commonService
    ) {
        this.utilsMessage = utilsMessage;
        this.updateProducer = updateProducer;
        this.utilsSendMessage = utilsSendMessage;
        this.commonService = commonService;
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

    private Long getCharIdFromUpdate(Update update) {
        return update.hasCallbackQuery()
                ? update.getCallbackQuery().getMessage().getChatId()
                : update.getMessage().getChatId();
    }

    private String getTextMessFromUpdate(Update update) {
        return update.hasCallbackQuery()
                ? update.getCallbackQuery().getMessage().getText()
                : update.getMessage().getText();
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
                throw new Exception("Internal error");
            }

            if (update.hasMessage()) {
                if (!update.hasMessage() || !update.getMessage().hasText()) {
                    return;
                }

                distrtMessagesBytype(update);
            } else if (update.hasCallbackQuery()) {
                distributeCallbackQueryMessages(update);
            }
        } catch (Exception e) {
            var chartId = getCharIdFromUpdate(update);

            var sendMessage = utilsMessage.generateSendMessageWithText(chartId, e.getMessage());
            telegramBot.sendAnswerMessage(sendMessage);
        }
    }

    /**
     * Менеджер текстовых сообщений
     * @param update
     * @throws Exception
     */
    private void distrtMessagesBytype(Update update) throws Exception {

        String textMess = getTextMessFromUpdate(update);
        Long charId = getCharIdFromUpdate(update);

        var structureCommand = utilsSendMessage.getStructureCommand(textMess);
        var enumType = structureCommand.getEnumTypeMessage();

        if ( enumType == EnumTypeMessage.TEXT_MESSAGE) {
            sendTextMessage(charId, structureCommand);
        } else if (enumType == EnumTypeMessage.BTMMENU) {
            distributeMenu(charId, textMess);
        } else {
            throw new Exception("Internal error");
        }

    }

    private void distributeMenu(Long chartId, String textMess) throws Exception {
        var structureCommand = utilsSendMessage.getStructureCommand(textMess);

        var sendMessage = utilsMessage.generateSendMessageWithBtn(chartId, structureCommand);

        telegramBot.sendAnswerMessage(sendMessage);

    }

    /**
     * Обработка collback команд
     * @param update
     */
    private void distributeCallbackQueryMessages(Update update) throws Exception {
        var textQuery = update.getCallbackQuery().getData();
        var chartId = getCharIdFromUpdate(update);

        var structureCommand = utilsSendMessage.getStructureCommand(textQuery);

        if (structureCommand.getEnumTypeMessage() == EnumTypeMessage.TEXT_MESSAGE) {
            sendTextMessage(chartId, structureCommand);
        } else if (structureCommand.getEnumTypeMessage() == EnumTypeMessage.FROM_DB) {

            var sendMessage = commonService.distributeStrCommand(chartId, structureCommand);

            telegramBot.sendAnswerMessage(sendMessage);

        } else {
            distributeMenu(chartId, textQuery);
        }

    }

    private void sendTextMessage(Long charId, DataFromParser structureCommand) throws Exception {

        var dataFromFile = FileAPI.readDataFromFile(structureCommand.getSource());

        if (!dataFromFile.RESULT) {
            throw new Exception("Контент не найден");
        }

        var txtMessage = (String) dataFromFile.VALUE;

        var sendMessage = utilsMessage.generateSendMessageWithText(charId, txtMessage);

        sendMessage.setParseMode(ParseMode.MARKDOWN);
        telegramBot.sendAnswerMessage(sendMessage);
    }

    public void setView(SendMessage sendMessage) {
        telegramBot.sendAnswerMessage(sendMessage);
    }

}

