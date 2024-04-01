package ru.animals.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;

import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.animals.service.UpdateProducer;
import ru.animals.utils.MessageUtils;
import ru.animals.utilsDEVL.FileAPI;

@Component
@Log4j
public class UpdateController {
    private TelegramBot telegramBot;
    private MessageUtils messageUtils;
    private UpdateProducer updateProducer;

    public UpdateController(MessageUtils messageUtils, UpdateProducer updateProducer) {
        this.messageUtils = messageUtils;
        this.updateProducer = updateProducer;
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
            var message = update.getMessage();

            // TODO: встроить процедуру проверки загрузки и исп. *.md file
            var dataFromFile = FileAPI.readDataFromFile("hello.md");

            String txtMessage;

            if (!dataFromFile.RESULT) {
                txtMessage = dataFromFile.MESSAGE;
            } else {
                txtMessage = (String) dataFromFile.VALUE;
            }


            var sendMessage = messageUtils.generateSendMessageWithText(update, txtMessage);
                    /*messageUtils.generateSendMessageWithText(update,
                    "Сообщение принято");*/

            sendMessage.setParseMode(ParseMode.MARKDOWN);

            telegramBot.sendAnswerMessage(sendMessage);

            /*if (message.hasText()) {
                processTextMessage(update);
            } else if (message.hasDocument()) {
                processDocMessage(update);
            } else if (message.hasPhoto()) {
                processPhotoMessage(update);
            } else {
                setUnsuppotedMessageTypeView(update);
            }*/
        }

    public void setView(SendMessage sendMessage) {
        telegramBot.sendAnswerMessage(sendMessage);
    }

    }

