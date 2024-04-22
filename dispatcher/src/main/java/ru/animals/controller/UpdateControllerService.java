package ru.animals.controller;

public interface UpdateControllerService {
    void sendButtonMenu(Long charId, String textMess) throws Exception;

    void sendTextMessageFromFile(Long charId, String fileSource) throws Exception;

}
