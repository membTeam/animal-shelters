package ru.animals.utils;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.animals.telegramComp.TelgramComp;
import ru.animals.utilsDEVL.DataFromParser;
import ru.animals.utilsDEVL.FileAPI;
import ru.animals.utilsDEVL.entitiesenum.EnumTypeFile;

import java.io.IOException;


@Component
public class UtilsMessage {

    public SendMessage generateSendMessageWithBtn(Update update,
                                      DataFromParser dataParser) throws Exception {

        var file = dataParser.getSource();
        var result = TelgramComp.sendMessageFromJSON(file);
        var sendMessage = (SendMessage) result.VALUE;
        sendMessage.setChatId(update.getMessage().getChatId().toString());

        var text = sendMessage.getText();

        if (text.startsWith("file:")) {
            var index = text.indexOf(":");
            var fileMes = text.substring(++index);

            if (FileAPI.getTypeFile(fileMes) != EnumTypeFile.TEXT) {
                throw new Exception("Не соответствие типа файла");
            }

            var textFromFile = FileAPI.readDataFromFile(fileMes);
            if (!textFromFile.RESULT) {
                throw new Exception(textFromFile.MESSAGE);
            }

            sendMessage.setText( (String) textFromFile.VALUE);
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
