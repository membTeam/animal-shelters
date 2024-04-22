package ru.animals.collbackCommand;

import ru.animals.collbackCommand.impl.BaseObject;
import ru.animals.repository.UserBotRepository;
import ru.animals.repository.VolunteerRepository;
import ru.animals.utilsDEVL.ValueFromMethod;

public interface DistrCollbackCommand {
    VolunteerRepository getVolunteerRepository();
    UserBotRepository getUserBotRepository();

    ValueFromMethod<BaseObject> preparationClass(String strClass);
}
