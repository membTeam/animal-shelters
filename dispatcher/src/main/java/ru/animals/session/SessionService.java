package ru.animals.session;

import lombok.Getter;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.animals.utils.DevlAPI;
import ru.animals.utilsDEVL.ValueFromMethod;
import ru.animals.utilsDEVL.entitiesenum.EnumTypeAppeal;

import java.util.HashMap;
import java.util.Map;

@Service
@Getter
public class SessionService {

    private Map<Long, BaseStateSession> mapItems = new HashMap<>();

/*    private ValueFromMethod getChatIdFromUpdate(Update update) {

        if (update == null || (!update.hasMessage() || !update.hasCallbackQuery())) {
            return new ValueFromMethod(false, "update не определен");
        }

        long chatId = update.hasMessage()
                ? update.getMessage().getChatId()
                : update.getCallbackQuery().getMessage().getChatId();

        return new ValueFromMethod<Long>(chatId);
    }*/

    private ValueFromMethod addSessionRegister(long chatId) {
        if (mapItems.containsKey(chatId)) {
            return new ValueFromMethod("Повторное обращение");
        }

        SessionRegister sessionRegister = new SessionRegister(chatId);
        mapItems.put(chatId, sessionRegister);

        return new ValueFromMethod(true, "ok");
    }

    private ValueFromMethod addSessionReport(long chatId) {

        if (mapItems.containsKey(chatId)) {
            return new ValueFromMethod("Повторное обращение");
        }

        SessionReport sessionReport = new SessionReport(chatId);
        mapItems.put(chatId, sessionReport);

        return new ValueFromMethod(true, "ok");
    }

    private ValueFromMethod addSessionObject(Update update, EnumTypeAppeal typeAppeal) {

        ValueFromMethod<Long> valueChatId = DevlAPI.getChatIdFromUpdate(update);

        if (!valueChatId.RESULT) {
            return new ValueFromMethod(false, "chatId не определен");
        }

        Long chatid = valueChatId.getValue();

        if (typeAppeal == EnumTypeAppeal.REGUSTER_USER) {
            return addSessionRegister(chatid);
        } else {
            return addSessionReport(chatid);
        }

    }

    public SendMessage distributionUpdate(Update update) {

        ValueFromMethod<Long> valueChatId = DevlAPI.getChatIdFromUpdate(update);

        if (!valueChatId.RESULT) {
            return null;
        }

        Long chatId =  valueChatId.getValue();

        if (!mapItems.containsKey(chatId)) {

        }

        return null;
    }
}
