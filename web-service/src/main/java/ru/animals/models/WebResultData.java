package ru.animals.models;

import lombok.Getter;

import org.springframework.http.HttpHeaders;


@Getter
public final class WebResultData {

    private boolean result;
    private String message;

    private byte[] byteData;
    private HttpHeaders httpHeaders;

    public WebResultData(String mes) {
        result = false;
        message = mes;
    }

    public WebResultData(byte[] bytes, HttpHeaders httpHeaders) {
        result = true;
        message = "ok";

        this.byteData = bytes;
        this.httpHeaders = httpHeaders;
    }

}
