package ru.animals.session.stateImpl.temp;

import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.animals.utilsDEVL.entitiesenum.EnumTypeAppeal;

@Getter
@Setter
public abstract class BaseMessage {
    private boolean status = false;
    private boolean result = false;
    private SendMessage sendMessage;
    private String textDefault;
    private long chatId;

    public BaseMessage(String strMess) {
        textDefault = strMess;
    }

    public abstract SendMessage getMessage(Update update, EnumTypeAppeal typeAppeal);

}
