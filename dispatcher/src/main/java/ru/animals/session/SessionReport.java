package ru.animals.session;

import lombok.Getter;
import ru.animals.utilsDEVL.entitiesenum.EnumTypeAppeal;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class SessionReport {
    private Long charId;
    private LocalDateTime lastAppeal;
    private boolean status = false;

    private List<BaseMessage> itemAppeal;

    public SessionReport(long chatId, EnumTypeAppeal typeAppeal) {
        this.charId = chatId;
        lastAppeal = LocalDateTime.now();

        if (typeAppeal == EnumTypeAppeal.REGUSTER_USER) {
            itemAppeal = List.of(
                // TODO: заменить строку приглашение
                new ReportMessage("введите данные согласно шаблона ")
            );
        }
    }

}
