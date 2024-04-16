package ru.animals.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.animals.collbackCommand.CommonCollbackService;
import ru.animals.service.serviceRepostory.CommonService;
import ru.animals.service.serviceRepostory.UpdateProducer;

//import ru.animals.service.serviceRepostory.VolunteersService;

import ru.animals.utils.UtilsMessage;
import ru.animals.utils.UtilsSendMessage;
import ru.animals.utilsDEVL.FileAPI;
import ru.animals.utilsDEVL.ValueFromMethod;
import ru.animals.utilsDEVL.entitiesenum.EnumTypeParamMessage;
import ru.animals.utilsDEVL.entitiesenum.EnumTypeParamCollback;

@Component
@Log4j
public class UpdateController {
    private TelegramBot telegramBot;
    private UtilsMessage utilsMessage;
    private UpdateProducer updateProducer;
    private UtilsSendMessage utilsSendMessage;
    private CommonService commonService;
    private CommonCollbackService commonCollbackService;

    public UpdateController(UtilsMessage utilsMessage,
                            UpdateProducer updateProducer,
                            UtilsSendMessage utilsSendMessage, CommonService commonService, CommonCollbackService commonCollbackService
    ) {
        this.utilsMessage = utilsMessage;
        this.updateProducer = updateProducer;
        this.utilsSendMessage = utilsSendMessage;
        this.commonService = commonService;
        this.commonCollbackService = commonCollbackService;
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

        if ( enumType == EnumTypeParamMessage.TEXT_MESSAGE) {
            sendTextMessage(charId, structureCommand.getSource());
        } else if (enumType == EnumTypeParamMessage.BTMMENU
                        || enumType == EnumTypeParamMessage.START) {
            distributeMenu(charId, textMess);
        } else {
            throw new Exception("The command was not found");
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

