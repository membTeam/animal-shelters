package ru.animals.session;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.animals.utilsDEVL.entitiesenum.EnumTypeAppeal;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SessionReport extends BaseStateSession {

    private EnumTypeAppeal typeAppeal;

    public SessionReport(long chatId) {
        super();
        setCharId(chatId);
        setLastAppeal(LocalDateTime.now());

        setItemAppeal(
                List.of(
                        new SessionState("Укажите самочувствие"),
                        new SessionState("Отвыкание от прежних привычек"),
                        new SessionState("вставьте фотографию")
                )
        );

    }

    @Override
    public BaseStateSession sendMessage(Update update) {
        return this;
    }
}
