package ru.animals.session.stateImpl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.animals.session.SessionServiceImpl;
import ru.animals.utilsDEVL.entitiesenum.EnumTypeAppeal;

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
    public SendMessage getSendMessage(SessionServiceImpl sessionService, Update update) {
     return null;
    }

    @Override
    public ResultState getResultState() {
        return null;
    }

}
