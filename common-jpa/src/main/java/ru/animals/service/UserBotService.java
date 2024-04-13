package ru.animals.service;

import ru.animals.entities.UserBot;

public interface UserBotService {
    UserBot addUserBot(Long chatId, String firstName);
}
