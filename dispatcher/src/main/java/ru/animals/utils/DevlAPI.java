package ru.animals.utils;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.animals.utilsDEVL.ValueFromMethod;
import ru.animals.utilsDEVL.entitiesenum.EnumTypeUpdate;

public class DevlAPI {

    private static ValueFromMethod verifyUpdate(Update update) {
        if (update == null || !(!update.hasMessage() || !update.hasCallbackQuery())) {
            return new ValueFromMethod(false, "argument update illegal");
        }

        return new ValueFromMethod(true, "ok");
    }

    public static EnumTypeUpdate typeUpdate(Update update, boolean idVerify) {

        EnumTypeUpdate result = EnumTypeUpdate.NONE;

        if (!idVerify) {
            var verifyUpdate = verifyUpdate(update);
            if (!verifyUpdate.RESULT) {
                return result;
            }
        }

        if (update.hasMessage()) {
            result = EnumTypeUpdate.TEXT_MESSAGE;
        }

        if (update.hasCallbackQuery()) {
            result = EnumTypeUpdate.COLLBACK;
        }

        return result;
    }

    public static ValueFromMethod getChatIdFromUpdate(Update update) {

        var verifyUpdate = verifyUpdate(update);
        if (!verifyUpdate.RESULT) {
            return verifyUpdate;
        }

        long chatId = update.hasMessage()
                ? update.getMessage().getChatId()
                : update.getCallbackQuery().getMessage().getChatId();

        return new ValueFromMethod<Long>(chatId);

    }

    public static String getTextMessFromUpdate(Update update) {
        String text = update.hasCallbackQuery()
                ? update.getCallbackQuery().getData()
                : update.getMessage().getText();

        return text.charAt(0) == '/' ? text.substring(1): text;

    }

    public static String lowercaseFirstLetter(String word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
    }

}
