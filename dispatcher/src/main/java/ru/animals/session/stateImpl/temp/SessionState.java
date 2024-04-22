package ru.animals.session.stateImpl.temp;

import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.animals.utilsDEVL.entitiesenum.EnumTypeAppeal;

@Getter
@Setter
public class SessionState extends BaseMessage  {

    public SessionState(String strMes){
        super(strMes);
    }

    @Override
    public SendMessage getMessage(Update update, EnumTypeAppeal typeAppeal) {
        return null;
    }

}
