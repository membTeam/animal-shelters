package ru.animals.exceptions;

public class UploadFileException extends RuntimeException{

    public UploadFileException(String message) {
        super(message);
    }
}
