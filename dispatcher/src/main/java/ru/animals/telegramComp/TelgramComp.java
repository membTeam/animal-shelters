package ru.animals.telegramComp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.animals.utilsDEVL.FileAPI;
import ru.animals.utilsDEVL.ValueFromMethod;

import java.io.IOException;

public class TelgramComp {

    public static SendMessage defaultSendMessage(long chatId, String text) {
        var sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);

        return sendMessage;
    }

    public static ValueFromMethod<SendMessage> sendMessageFromJSON(String fileJson) {

        try {
            var dataFromFile = FileAPI.readDataFromFile(fileJson);

            if (!dataFromFile.RESULT) {
                return new ValueFromMethod<>(false, dataFromFile.MESSAGE) ;
            }

            var mapper = new ObjectMapper();
            var json = (String) dataFromFile.VALUE;

            var sendMessage = mapper.readValue(json, SendMessage.class);

            return new ValueFromMethod<SendMessage>(sendMessage);

        } catch (IOException e) {
            return new ValueFromMethod(false, e.getMessage());
        }

    }

    public static ValueFromMethod buttonMenuFromJSON(String fileJson) {

        try {
            var dataFromFile = FileAPI.readDataFromFile(fileJson);

            if (!dataFromFile.RESULT) {
                return dataFromFile;
            }

            var mapper = new ObjectMapper();
            var json = (String) dataFromFile.VALUE;

            var inlineKeyboardMarkup = mapper.readValue(json, InlineKeyboardMarkup.class);

            return new ValueFromMethod(inlineKeyboardMarkup);

        } catch (IOException e) {
            return new ValueFromMethod(false, e.getMessage());
        }

    }

}
