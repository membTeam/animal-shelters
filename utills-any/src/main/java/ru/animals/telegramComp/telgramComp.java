package ru.animals.telegramComp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.animals.utilsDEVL.FileAPI;
import ru.animals.utilsDEVL.ValueFromMethod;

import java.io.IOException;

public class telgramComp {

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
