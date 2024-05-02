package ru.animals.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.io.IOException;

import ru.animals.collbackCommand.DistrCollbackCommandImpl;
import ru.animals.service.CommonService;
import ru.animals.service.ServUserBot;
import ru.animals.service.UpdateProducer;
import ru.animals.session.SessionServiceImpl;
import ru.animals.utils.DevlAPI;
import ru.animals.utils.UtilsMessage;
import ru.animals.utils.UtilsSendMessage;
import ru.animals.utilsDEVL.FileAPI;
import ru.animals.utilsDEVL.ValueFromMethod;
import ru.animals.utilsDEVL.entitiesenum.EnumTypeParamCollback;
import ru.animals.service.ServParsingStrPhotoNext;


@Log4j
@Component
@RequiredArgsConstructor
public class UpdateController {
    private TelegramBot telegramBot;
    private final UtilsMessage utilsMessage;
    private final UpdateProducer updateProducer;
    private final UtilsSendMessage utilsSendMessage;
    private final CommonService commonService;
    private final DistrCollbackCommandImpl commonCollbackService;
    private final ServUserBot servUserBot;
    private final SessionServiceImpl sessionServiceUpdate;
    private final ServParsingStrPhotoNext servParsingStrPhoto;

    public void registerBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
        sessionServiceUpdate.init(this.telegramBot);
    }

    private Long getCharIdFromUpdate(Update update) throws Exception {

        ValueFromMethod<Long> res = DevlAPI.getChatIdFromUpdate(update);

        if (!res.RESULT) {
            throw new Exception(res.MESSAGE);
        }

        return res.getValue();
    }

    /**
     * Обработка Update from Telegram bot
     * @param update
     */
    public void distributeMessages(Update update) throws Exception {

        if (update == null) { return;  }

        try {
            if (utilsSendMessage.isERROR()) {
                log.error(utilsSendMessage.getMessageErr());
                throw new Exception("Internal error");
            }

            /**
             * Перехватывает сообщения диалога с пользователем
             */
            if (sessionServiceUpdate.isExistsStateSession(update)) {
                telegramBot.sendAnswerMessage(sessionServiceUpdate.distributionUpdate(update));
            } else {

                /**
                 * перехватывает сообщения, поступившие от волонтера при проверке отчета
                 * после обработки сообщения от волонетера
                 * продолжится обработка поступившего сообщения от telegram bot
                 */
                var messageForUser = commonService.messageForUser(getCharIdFromUpdate(update));
                if (messageForUser != null) {
                    messageForUser.forEach(item -> telegramBot.sendAnswerMessage(item));
                }

                switch (DevlAPI.typeUpdate(update, false)) {
                    case TEXT_MESSAGE -> distributeMessagesBytype(update);
                    case COLLBACK -> distributeCallbackQueryMessages(update);
                    default -> {
                        throw new IllegalArgumentException("Тип не определен");
                    }
                }
            }

        } catch (Exception e) {
            try {
                var chartId = getCharIdFromUpdate(update);
                var mesText = "Processing failure on the server";
                var sendMessage = utilsMessage.generateSendMessageWithText(chartId, mesText);

                log.error(mesText);

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

        String textMess = DevlAPI.getTextMessFromUpdate(update); // getTextMessFromUpdate(update);
        Long charId = getCharIdFromUpdate(update);

        var structureCommand = utilsSendMessage.getStructureCommand(textMess);
        switch (structureCommand.getEnumTypeMessage()) {
            case BTMMENU -> sendButtonMenu(charId, textMess);
            case TEXT_MESSAGE -> sendTextMessageFromFile(charId, structureCommand.getSource());
            case SELMENU -> sendButtonMenuByParse(charId);
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
    private void sendButtonMenuByParse(Long charId) throws Exception {
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

        sendButtonMenu(charId, btnMenuStart);
    }


    /**
     * Обработка collback команд
     * @param update
     */
    private void distributeCallbackQueryMessages(Update update) throws Exception {
        var textQuery = update.getCallbackQuery().getData();
        var chartId = getCharIdFromUpdate(update);

        if (textQuery.substring(0, 3).equals("pht")) {
            sendPhotoMessage(update);
            return;
        }

        if (textQuery.equals("txt-start")) {
            sendButtonMenuByParse(chartId);
            return;
        }

        var structCollbackCommand = utilsSendMessage.getStructCommandCollback(textQuery);
        var enumType = structCollbackCommand.getEnumTypeParameter();

        if (enumType == EnumTypeParamCollback.TCL_TXT) {
            /**
             * Создание текстового сообщения
             */
            sendTextMessageFromFile(chartId,
                    utilsSendMessage.getStructureCommand(structCollbackCommand).getSource());
        } else if (enumType == EnumTypeParamCollback.TCL_BTN) {
            /**
             * Создание кнопочного меню
             */
            sendButtonMenu(chartId,
                    utilsSendMessage.getStructureCommand(structCollbackCommand).getCommand());
        } else if (enumType == EnumTypeParamCollback.TCL_DBD) {
            /**
             * Взаимодействие с БД
             */
            var sendMessage = commonCollbackService.distributeStrCommand(chartId, structCollbackCommand);
            telegramBot.sendAnswerMessage(sendMessage);
        } else if (enumType == EnumTypeParamCollback.TCL_DST){
            /**
             * процедура сдачи отчета
             */
            var sendMessge = sessionServiceUpdate.distributionUpdate(update);
            telegramBot.sendAnswerMessage(sendMessge);
        }else {
            throw new UnsupportedOperationException("Команда не определена");
        }
    }

    public void sendPhotoMessage(Update update) throws Exception {
        long strChatId = update.getCallbackQuery().getMessage().getChatId();
        var collbackData = update.getCallbackQuery().getData();

        var resData = servParsingStrPhoto.getPathPhoto(collbackData);

        var index = resData.indexOf("##");
        var caption = resData.substring(0, index);
        var filePath = resData.substring(index + 2);

        telegramBot.sendPhotoMessage(strChatId, filePath, caption);
    }

    public void sendButtonMenu(Long charId, String textMess) throws Exception {
        var structureCommand = utilsSendMessage.getStructureCommand(textMess);

        var sendMessage = utilsMessage.generateSendMessageWithBtn(charId, structureCommand);

        telegramBot.sendAnswerMessage(sendMessage);

    }

    public void sendTextMessageFromFile(Long charId,
                                         String fileSource) throws IOException {
        ValueFromMethod<String> dataFromFile = FileAPI.readDataFromFile(fileSource);

        if (!dataFromFile.RESULT) {
            throw new IOException("Контент не найден");
        }

        var txtMessage = dataFromFile.getValue();
        var sendMessage = utilsMessage.generateSendMessageWithText(charId, txtMessage);

        sendMessage.setParseMode(ParseMode.MARKDOWN);
        telegramBot.sendAnswerMessage(sendMessage);
    }

}

