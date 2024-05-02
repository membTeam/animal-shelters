package ru.animals.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;


@Component
@Log4j
public class TelegramBot extends TelegramLongPollingBot{

    private String userName;

    private UpdateController updateController;

    public TelegramBot(UpdateController updateController,
                       @Value("${bot.token}") String botToken,
                       @Value("${bot.username}") String username ) {
        super(botToken);
        this.updateController = updateController;
        this.userName = username;
    }

    @PostConstruct
    public void init() {
        updateController.registerBot(this);
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            updateController.distributeMessages(update);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void sendPhotoMessage(long chatId, String strPath, String caption) throws Exception {

        InputFile photo = new InputFile(new java.io.File(strPath));

        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setCaption(caption);
        sendPhoto.setChatId(chatId);
        sendPhoto.setPhoto(photo);

        execute(sendPhoto);
    }

    public File downloadFile(Update update, java.io.File fileDist) throws TelegramApiException {
        var telegramMess = update.getMessage();

        var photoSizeCount = telegramMess.getPhoto().size();
        var photoIndex = photoSizeCount > 1 ? telegramMess.getPhoto().size() - 1 : 0;
        var telegramPhoto = photoIndex > 4
                ? telegramMess.getPhoto().get(4)
                : telegramMess.getPhoto().get(photoIndex);

        GetFile getFile = new GetFile(telegramPhoto.getFileId());
        File file = execute(getFile);

        downloadFile(file, fileDist);

        return file;

    }

    public void sendAnswerMessage(SendMessage message) {
        if (message != null) {
            try {
                execute(message);
            } catch (TelegramApiException ex) {
                log.error(ex.getMessage());
            }
        }
    }

    @Override
    public String getBotUsername() {
        return userName;
    }

}
