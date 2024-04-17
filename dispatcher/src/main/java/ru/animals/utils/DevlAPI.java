package ru.animals.utils;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.animals.utilsDEVL.ValueFromMethod;
import ru.animals.utilsDEVL.entitiesenum.EnumTypeMess;

public class DevlAPI {

    private static ValueFromMethod verifyUpdate(Update update) {
        if (update == null || (!update.hasMessage() || !update.hasCallbackQuery())) {
            return new ValueFromMethod(false, "argument update illegal");
        }

        return new ValueFromMethod(true, "ok");
    }

    public static EnumTypeMess getTypeMessage(Update update, boolean idVerify) {

        EnumTypeMess result = EnumTypeMess.NONE;

        if (!idVerify) {
            var verifyUpdate = verifyUpdate(update);
            if (!verifyUpdate.RESULT) {
                return result;
            }
        }

        if (update.hasMessage()) {
            result = EnumTypeMess.TEXT_MESSAGE;
        }

        if (update.hasCallbackQuery()) {
            result = EnumTypeMess.COLLBACK;
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

}
