package ru.animals.controller;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateControllerService {
    void sendButtonMenu(Long charId, String textMess) throws Exception;

    void sendTextMessageFromFile(Long charId, String fileSource) throws Exception;
}
