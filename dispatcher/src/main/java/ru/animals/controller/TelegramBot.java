package ru.animals.controller;

import lombok.extern.log4j.Log4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.stickers.SetStickerSetThumbnail;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;


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


    public String downloadFile(Update update, String imageStorageDirReport) throws TelegramApiException {
        var telegramMess = update.getMessage();

        var photoSizeCount = telegramMess.getPhoto().size();
        var photoIndex = photoSizeCount > 1 ? telegramMess.getPhoto().size() - 1 : 0;
        var telegramPhoto = photoIndex > 4
                ? telegramMess.getPhoto().get(4)
                : telegramMess.getPhoto().get(photoIndex);

        GetFile getFile = new GetFile(telegramPhoto.getFileId());
        File file = execute(getFile);

        var strFile = file.getFilePath();
        final String fileExt = strFile
                .substring( strFile.lastIndexOf(".") + 1);

        var chatId = telegramMess.getChat().getId();
        var strFileDistination = String.format("rep-%d.%s", chatId, fileExt);

        var strFilePath = Path.of(imageStorageDirReport, strFileDistination).toString();

        downloadFile(file, new java.io.File(strFilePath));

        return "Файл загружен.";

    }

    public void downloadFile(File fileTelegram, String path) throws TelegramApiException {
        var strPathFromTelegram = fileTelegram.getFilePath();
        strPathFromTelegram = strPathFromTelegram.substring( strPathFromTelegram.indexOf('/'));

        var strPath = "/home/osnuser/IdeaProjects/imagePrAnimals/animationReport"+strPathFromTelegram;
        var file = new java.io.File(strPath); // photos/file_5.jpg

        downloadFile(fileTelegram, file);
    }

    public File getFile(String fileId) throws TelegramApiException {
        GetFile getFile = new GetFile(fileId);
        return execute(getFile);
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

    @Override
    public Boolean execute(SetStickerSetThumbnail setStickerSetThumbnail) throws TelegramApiException {
        return null;
    }

    @Override
    public CompletableFuture<Boolean> executeAsync(SetStickerSetThumbnail setStickerSetThumbnail) {
        return null;
    }

}
