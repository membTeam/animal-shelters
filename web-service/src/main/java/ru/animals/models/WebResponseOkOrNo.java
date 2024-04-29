package ru.animals.models;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WebResponseOkOrNo {
    private final boolean result;
    private final String message;

}
