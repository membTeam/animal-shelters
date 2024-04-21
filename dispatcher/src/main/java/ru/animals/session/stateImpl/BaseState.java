package ru.animals.session.stateImpl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.animals.session.stateImpl.temp.SessionService;

@Getter
@Setter
@NoArgsConstructor
public abstract class BaseState {
    public abstract SendMessage getSendMessage(SessionService sessionService, Update update);
}
