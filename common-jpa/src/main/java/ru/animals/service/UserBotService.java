package ru.animals.service;

import ru.animals.entities.UserBot;
import ru.animals.entities.enumEntity.EnumRoleUser;
import ru.animals.entities.enumEntity.EnumState;

public interface UserBotService {
    UserBot addUserBot(Long chatId, String firstName, EnumRoleUser role, EnumState state);
}
