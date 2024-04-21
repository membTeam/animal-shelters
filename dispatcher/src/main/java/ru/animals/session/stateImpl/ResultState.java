package ru.animals.session.stateImpl;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
public final class ResultState {

    public final LocalDateTime LAST_APPEAL;
    public final Long CHAT_ID;
    public final boolean RESULT_END;
    public final boolean START_STATE;

}
