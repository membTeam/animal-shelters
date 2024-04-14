package ru.animals.session;

import lombok.Getter;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.animals.utilsDEVL.ValueFromMethod;
import ru.animals.utilsDEVL.entitiesenum.EnumTypeAppeal;

import java.util.HashMap;
import java.util.Map;

@Service
@Getter
public class SessionService {

    private Map<Long, SessionReport> mapItems = new HashMap<>();

    public ValueFromMethod addSessionObject(Update update , EnumTypeAppeal typeAppeal) {
        long chatid = update.hasMessage()
                ? update.getMessage().getChatId()
                : update.getCallbackQuery().getMessage().getChatId();

        if (mapItems.containsKey(chatid)) {
            return new ValueFromMethod("Повторное обращение");
        }

        return null;
    }

    public SendMessage distributionUpdate(Update update) {

        return null;
    }
}
