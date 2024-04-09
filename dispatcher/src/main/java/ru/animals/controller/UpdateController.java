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

    /**
     * Убирает начальный /
     * @param text
     * @return
     */
    private String modifingTextMessage(String text) {
        return text.charAt(0) == '/' ? text.substring(1) : text;
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
                throw new Exception("Нет буфера команд");
            }

            if (update.hasMessage()) {
                distributeTextMessages(update);
            } else if (update.hasCallbackQuery()) {
                distributeCallbackQueryMessages(update);
            }
        } catch (Exception e) {
            var sendMessage = utilsMessage.generateSendMessageWithText(update, e.getMessage());
            telegramBot.sendAnswerMessage(sendMessage);
        }
    }

    /**
     * Менеджер текстовых сообщений
     * @param update
     * @throws Exception
     */
    private void distributeTextMessages(Update update) throws Exception {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }

        var textMess = modifingTextMessage(modifingTextMessage(update.getMessage().getText()));

        if (textMess.equals("start")) {
            distributeMenu(update);
            return;
        }

        var structureCommand = utilsSendMessage.getDataCommand(textMess);

        if (structureCommand.getEnumTypeMessage() == EnumTypeMessage.TEXT_message) {
            sendTextMessage(update, structureCommand);
        } else {
            distributeMenu(update);
        }

    }

    private void distributeMenu(Update update) throws Exception {

        var textMess = modifingTextMessage(update.getMessage().getText());
        var structureCommand = utilsSendMessage.getDataCommand(textMess);

        var sendMessage = utilsMessage.generateSendMessageWithBtn(update, structureCommand);

        telegramBot.sendAnswerMessage(sendMessage);

    }

    /**
     * Обработка collback команд
     * @param update
     */
    private void distributeCallbackQueryMessages(Update update) {
        var textMessage = utilsMessage.generateSendMessageWithText(update, "Сервис не определен");
        telegramBot.sendAnswerMessage(textMessage);
    }

    private void sendTextMessage(Update update, DataFromParser structureCommand) throws Exception {

        var dataFromFile = FileAPI.readDataFromFile(structureCommand.getSource());

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

