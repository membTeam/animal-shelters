package ru.animals.session;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.animals.utilsDEVL.entitiesenum.EnumTypeAppeal;

@Getter
@Setter
public class ReportMessage extends BaseMessage  {

    public ReportMessage(String strMes){
        super(strMes);
    }

    @Override
    public SendMessage run(Update update, EnumTypeAppeal typeAppeal) {
        return null;
    }

}
