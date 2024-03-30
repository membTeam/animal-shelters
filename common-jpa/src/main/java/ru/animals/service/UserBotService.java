package ru.animals.service;

import ru.animals.models.UserBot;
import ru.animals.models.enumEntity.EnumRoleUser;
import ru.animals.models.enumEntity.EnumState;

public interface UserBotService {
    UserBot addUserBot(Long chatId, String firstName, EnumRoleUser role, EnumState state);
}
