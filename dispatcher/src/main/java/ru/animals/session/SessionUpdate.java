package ru.animals.session;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface SessionUpdate {
    SendMessage distributionUpdate(Update update) throws Exception;
}
