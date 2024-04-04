package ru.animals.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;

import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.animals.service.UpdateProducer;
import ru.animals.utils.MessageUtils;
import ru.animals.utils.ParsingCommand;
import ru.animals.utils.ParsingMessage;
import ru.animals.utilsDEVL.DataFromParser;
import ru.animals.utilsDEVL.FileAPI;

@Component
@Log4j
public class UpdateController {
    private TelegramBot telegramBot;
    private MessageUtils messageUtils;
    private UpdateProducer updateProducer;
    private ParsingCommand parsingCommand;

    public UpdateController(MessageUtils messageUtils, UpdateProducer updateProducer, ParsingCommand parsingCommand) {
        this.messageUtils = messageUtils;
        this.updateProducer = updateProducer;
        this.parsingCommand = parsingCommand;
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
            var textMess = update.getMessage().getText();
            if (textMess.charAt(0) == '/') {
                textMess = textMess.substring(1);
            }

            var strSource = parsingCommand.getSource(textMess);

            if (strSource.equals("empty")) {
                var sendMessage = messageUtils.generateSendMessageWithText(update, "Нет такой команды");
                telegramBot.sendAnswerMessage(sendMessage);
                return;
            }

            var dataFromFile = FileAPI.readDataFromFile(strSource);

            if (!dataFromFile.RESULT) {
                // txtMessage = dataFromFile.MESSAGE;
                // TODO: переработать
                return;
            }

            var txtMessage = (String) dataFromFile.VALUE;

            var sendMessage = messageUtils.generateSendMessageWithText(update, txtMessage );

            sendMessage.setParseMode(ParseMode.MARKDOWN);
            telegramBot.sendAnswerMessage(sendMessage);

        }

    public void setView(SendMessage sendMessage) {
        telegramBot.sendAnswerMessage(sendMessage);
    }

    }

