package ru.animals.exeption;

public class WebException extends RuntimeException{
    public WebException(String message) {
        super(message);
    }
}
