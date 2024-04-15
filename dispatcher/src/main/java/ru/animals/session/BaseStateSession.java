package ru.animals.session;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public abstract class BaseStateSession {
    private Long charId;
    private LocalDateTime lastAppeal;
    private boolean status = false;

    private List<BaseMessage> itemAppeal;

    public abstract BaseStateSession sendMessage(Update update);

}
