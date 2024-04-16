package ru.animals.service.serviceRepostory;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface AnswerConsumer {
    void consume(SendMessage sendMessage);
}
