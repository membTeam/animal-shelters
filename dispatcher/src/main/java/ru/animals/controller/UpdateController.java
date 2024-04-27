package ru.animals.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
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

@Log4j
@Component
@RequiredArgsConstructor
public class UpdateController implements UpdateControllerService {
    private TelegramBot telegramBot;
    private final UtilsMessage utilsMessage;
    private final UpdateProducer updateProducer;
    private final UtilsSendMessage utilsSendMessage;
    private final CommonService commonService;
    private final DistrCollbackCommandImpl commonCollbackService;
    private final ServUserBot servUserBot;
    private final SessionServiceImpl sessionServiceUpdate;


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
     * Менеджер команд в зависимости от типа сообщения
     * @param update
     */
    public void distributeMessages(Update update) throws Exception {

        if (update == null) { return;  }

        try {
            if (utilsSendMessage.isERROR()) {
                log.error(utilsSendMessage.getMessageErr());
                throw new Exception("Internal error");
            }

            if (sessionServiceUpdate.isExistsStateSession(update)) {
                telegramBot.sendAnswerMessage(sessionServiceUpdate.distributionUpdate(update));
            } else {
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
            case TEXT_MESSAGE -> sendTextMessageFromFile(charId, textMess);
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

        var structCollbackCommand = utilsSendMessage.getStructCommandCollback(textQuery);

        var enumType = structCollbackCommand.getEnumTypeParameter();
        if (enumType == EnumTypeParamCollback.TCL_TXT) {
            sendTextMessageFromFile(chartId,
                    utilsSendMessage.getStructureCommand(structCollbackCommand).getSource());

        } else if (enumType == EnumTypeParamCollback.TCL_BTN) {
            sendButtonMenu(chartId,
                    utilsSendMessage.getStructureCommand(structCollbackCommand).getCommand());

        } else if (enumType == EnumTypeParamCollback.TCL_DBD) {
            // TODO: исправить идентификатор метода
            var sendMessage = commonCollbackService.distributeStrCommand(chartId, structCollbackCommand);

            telegramBot.sendAnswerMessage(sendMessage);

        } else if (enumType == EnumTypeParamCollback.TCL_DST){
            var sendMessge = sessionServiceUpdate.distributionUpdate(update);
            telegramBot.sendAnswerMessage(sendMessge);
        } else {
            sendButtonMenu(chartId, textQuery);
        }
    }

    @Override
    public void sendButtonMenu(Long charId, String textMess) throws Exception {
        var structureCommand = utilsSendMessage.getStructureCommand(textMess);

        var sendMessage = utilsMessage.generateSendMessageWithBtn(charId, structureCommand);

        telegramBot.sendAnswerMessage(sendMessage);

    }

    @Override
    public void sendTextMessageFromFile(Long charId,
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

    public TelegramBot getTelegramBot() {
        return telegramBot;
    }

    public void setView(SendMessage sendMessage) {
        telegramBot.sendAnswerMessage(sendMessage);
    }

}

