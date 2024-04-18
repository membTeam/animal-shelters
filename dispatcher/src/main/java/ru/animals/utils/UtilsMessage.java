package ru.animals.utils;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.animals.telegramComp.TelgramComp;
import ru.animals.utils.parser.StructForBaseConfig;
import ru.animals.utilsDEVL.FileAPI;
import ru.animals.utilsDEVL.entitiesenum.EnumTypeFile;

@Component
public class UtilsMessage {

    public SendMessage generateSendMessageWithBtn(Long chartId,
                                      StructForBaseConfig dataParser) throws Exception {

        var file = dataParser.getSource();
        var valueFromMethod = TelgramComp.sendMessageFromJSON(file);
        SendMessage sendMessage = valueFromMethod.getValue();

        sendMessage.setChatId(chartId);

        var text = sendMessage.getText();

        if (text.startsWith("file:")) {
            var index = text.indexOf(":");
            var fileMes = text.substring(++index);

            if (FileAPI.getTypeFile(fileMes) != EnumTypeFile.INF) {
                throw new Exception("Не соответствие типа файла");
            }

            var textFromFile = FileAPI.readDataFromFile(fileMes);
            if (!textFromFile.RESULT) {
                throw new Exception(textFromFile.MESSAGE);
            }

            sendMessage.setText((String) textFromFile.VALUE);
        }

        return sendMessage;
    }

    public SendMessage generateSendMessageWithText(Long chartId, String text) {
        var sendMessage = new SendMessage();

        sendMessage.setChatId(chartId);
        sendMessage.setText(text);

        return sendMessage;
    }
}
