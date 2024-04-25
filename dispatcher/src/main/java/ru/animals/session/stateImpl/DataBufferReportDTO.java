package ru.animals.session.stateImpl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public final class DataBufferReportDTO {
    private String strProperty;
    private String textMessage;
    private String strMethod;
}
