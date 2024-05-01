package ru.animals.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.animals.telegramComp.TelgramComp;
import ru.animals.utils.parser.StructForBaseConfig;
import ru.animals.utilsDEVL.FileAPI;
import ru.animals.utilsDEVL.ValueFromMethod;
import ru.animals.utilsDEVL.entitiesenum.EnumTypeFile;

import ru.animals.utilsDEVL.entitiesenum.EnumTypeConfCommand;

@Component
@RequiredArgsConstructor
public class UtilsMessage {
    private final UtilsSendMessage utilsSendMessage;

    public SendMessage generateSendMessageWithBtn(Long chatId,
                                      StructForBaseConfig dataParser) throws Exception {

        var file = dataParser.getSource();
        var valueFromMethod = TelgramComp.sendMessageFromJSON(file);
        SendMessage sendMessage = valueFromMethod.getValue();

        sendMessage.setChatId(chatId);

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

    public SendMessage generateSendMessageWithText(Long chatId, String text) {
        var sendMessage = new SendMessage();

        sendMessage.setChatId(chatId);
        sendMessage.setText(text);

        return sendMessage;
    }

    /**
     * SendMessage поздравление усыновителю
     * @param chatId
     * @return
     */
    public SendMessage generaleSendMessageCongratulation(Long chatId) {

        StructForBaseConfig structForBaseConfig = utilsSendMessage.getStructureForCongratulation();
        var fileSource = structForBaseConfig.getSource();
        var resLoad = FileAPI.readDataFromFile(fileSource);

        if (!resLoad.RESULT) {
            return TelgramComp.defaultSendMessage(chatId, "Нет файла поздарвления");
        }

        var sendMessage = generateSendMessageWithText(chatId, resLoad.getValue());

        sendMessage.setParseMode(ParseMode.MARKDOWN);

        return sendMessage;
    }

}
