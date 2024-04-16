package ru.animals.session;

import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.animals.utilsDEVL.entitiesenum.EnumTypeAppeal;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class SessionRegister extends BaseStateSession {

    private EnumTypeAppeal typeAppeal;

    public SessionRegister(long chatId) {
        setCharId(chatId);
        setLastAppeal(LocalDateTime.now());

        setItemAppeal(List.of(
                // TODO: заменить строку приглашение
                new SessionState("Введите данные согласно шаблона")
        ));


    }

    @Override
    public BaseStateSession sendMessage(Update update) {
        BaseMessage baseMessage = getItemAppeal()
                .stream()
                .filter(item-> !item.isResult()).findFirst().orElseThrow();
//        var result = baseMessage.setSendMessage();

        return this;
    }
}
