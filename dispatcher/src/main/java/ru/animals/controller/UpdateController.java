package ru.animals.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;

import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.animals.service.UpdateProducer;
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

    public UpdateController(UtilsMessage utilsMessage, UpdateProducer updateProducer, UtilsSendMessage utilsSendMessage) {
        this.utilsMessage = utilsMessage;
        this.updateProducer = updateProducer;
        this.utilsSendMessage = utilsSendMessage;
    }

    public void registerBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void processUpdate(Update update) {
        if (update == null) {
            log.error("Received update is null");
            return;
        }

        if (update.hasMessage()) {
            distributeMessagesByType(update);
        } else {
            log.error("Received unsuppoted message type is received: " + update);
        }
    }

    private void distributeMessagesByType(Update update) {

        try {
            if (utilsSendMessage.isERROR()) {
                throw new Exception("Нет данных по командам");
            }

            var textMess = update.getMessage().getText();
            if (textMess.charAt(0) == '/') {
                textMess = textMess.substring(1);
            }

            var structureCommand = utilsSendMessage.getDataCommand(textMess);

            if (structureCommand.getEnumTypeMessage().equals(EnumTypeMessage.TEXT_message)) {
                sendTextMessage(update, structureCommand.getCommand());
            }

        } catch (Exception e) {
            var sendMessage = utilsMessage.generateSendMessageWithText(update, e.getMessage());
            telegramBot.sendAnswerMessage(sendMessage);
        }
    }

    private void sendTextMessage(Update update, String strCommand) throws Exception {

        var fileSource = utilsSendMessage.getSource(strCommand);

        var dataFromFile = FileAPI.readDataFromFile(fileSource);
        if (!dataFromFile.RESULT) {
            throw new Exception("Контент не найден");
        }

        var txtMessage = (String) dataFromFile.VALUE;
        var sendMessage = utilsMessage.generateSendMessageWithText(update, txtMessage);

        sendMessage.setParseMode(ParseMode.MARKDOWN);
        telegramBot.sendAnswerMessage(sendMessage);

    }

    public void setView(SendMessage sendMessage) {
        telegramBot.sendAnswerMessage(sendMessage);
    }

}

