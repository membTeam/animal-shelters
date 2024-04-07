package ru.animals.utils;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.animals.telegramComp.TelgramComp;
import ru.animals.utilsDEVL.DataFromParser;
import ru.animals.utilsDEVL.FileAPI;


@Component
public class UtilsMessage {

    public SendMessage generateSendMessageWithBtn(Update update, DataFromParser dataParser) {

        var file = dataParser.getSource();
        var result = TelgramComp.sendMessageFromJSON(file);
        var sendMessage = (SendMessage) result.VALUE;
        sendMessage.setChatId(update.getMessage().getChatId().toString());

        var text = sendMessage.getText();

        if (text.startsWith("file:")) {
            var index = text.indexOf(":");
            var fileMes = text.substring(index);
        }

        return sendMessage;
    }

    public SendMessage generateSendMessageWithText(Update update, String text) {
        var message = update.getMessage();
        var sendMessage = new SendMessage();

        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(text);

        return sendMessage;
    }
}
