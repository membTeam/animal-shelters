package ru.animals.session.stateImpl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.animals.session.SessionServiceImpl;

@Getter
@Setter
@NoArgsConstructor
public abstract class BaseState {
    public abstract SendMessage getSendMessage(SessionServiceImpl sessionService, Update update);
    public abstract ResultState getResultState();
}
