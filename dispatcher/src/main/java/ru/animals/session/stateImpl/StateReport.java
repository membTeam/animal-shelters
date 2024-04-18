package ru.animals.session.stateImpl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.animals.session.SessionService;
import ru.animals.session.SessionState;
import ru.animals.utilsDEVL.entitiesenum.EnumTypeAppeal;

import java.time.LocalDateTime;
import java.util.List;

public class StateReport extends BaseState {

    private EnumTypeAppeal typeAppeal;

    public StateReport(long chatId) {
        super();
        /*setCharId(chatId);
        setLastAppeal(LocalDateTime.now());

        setItemAppeal(
                List.of(
                        new SessionState("Укажите самочувствие"),
                        new SessionState("Отвыкание от прежних привычек"),
                        new SessionState("вставьте фотографию")
                )
        );*/

    }

    @Override
    public SendMessage getSendMessage(SessionService sessionService, Update update) {
     return null;
    }
}
