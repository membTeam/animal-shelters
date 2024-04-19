package ru.animals.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
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
import ru.animals.exceptions.UploadFileException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import ru.animals.exceptions.UploadFileException;

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

    @Value("${service.file_info.url}")
    private String fileInfoUri;

    @Value("${service.file_storage.url}")
    String fileStorageUri;

    @Value("${bot.token}")
    private String token;

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

            if (update.getMessage().hasPhoto()) {
                distributePhoto(update);
                return;
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

// ------------------------ load photo
    private ResponseEntity<String> getFilePath(String fileId) {
        var restTemplate = new RestTemplate();
        var headers = new HttpHeaders();
        var request = new HttpEntity<>(headers);

        return restTemplate.exchange(
                fileInfoUri,
                HttpMethod.GET,
                request,
                String.class,
                token,
                fileId
        );

    }

    private String getFilePath(ResponseEntity<String> response) {
        var jsonObject = new JSONObject(response.getBody());
        return String.valueOf(jsonObject
                .getJSONObject("result")
                .getString("file_path"));
    }

    private byte[] downloadFile(String filePath) throws Exception {
        var fullUri = fileStorageUri.replace("{token}", token)
                .replace("{filePath}", filePath);
        URL urlObj = null;
        try {
            urlObj = new URL(fullUri);
        } catch (MalformedURLException e) {
            log.error(e.getMessage());
            throw new UploadFileException(e.getMessage());
        }

        try (InputStream is = urlObj.openStream()) {
            return is.readAllBytes();
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new UploadFileException(e.getMessage());
        }
    }

    private void distributePhoto(Update update) throws Exception {
        var telegramMessage = update.getMessage();

        var photoSizeCount = telegramMessage.getPhoto().size();
        var photoIndex = photoSizeCount > 1 ? telegramMessage.getPhoto().size() - 1 : 0;
        var telegramPhoto = telegramMessage.getPhoto().get(photoIndex);
        var fileId = telegramPhoto.getFileId();
        var response = getFilePath(fileId);

        if (response.getStatusCode() == HttpStatus.OK) {

            var filePath = getFilePath(response);
            var fileInByte = downloadFile(filePath);

            log.info("File download");
        } else {
            throw new UploadFileException("Error configue response");
        }

    }

    // -------------------- end load photo

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

