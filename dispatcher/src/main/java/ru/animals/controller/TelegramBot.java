package ru.animals.controller;


import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
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
                       @Value("${bot.username}") String username) {
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
        updateController.processUpdate(update);
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
