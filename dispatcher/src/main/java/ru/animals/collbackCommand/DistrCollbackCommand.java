package ru.animals.collbackCommand;

import ru.animals.collbackCommand.impl.BaseObject;
import ru.animals.repository.AnimalsRepository;
import ru.animals.repository.BreedsRepository;
import ru.animals.repository.UserBotRepository;
import ru.animals.repository.VolunteerRepository;
import ru.animals.utilsDEVL.ValueFromMethod;

public interface DistrCollbackCommand {
    AnimalsRepository getAnimalsRepository();
    BreedsRepository getBreedsRepository();

    VolunteerRepository getVolunteerRepository();
    UserBotRepository getUserBotRepository();

    ValueFromMethod<BaseObject> preparationClass(String strClass);
}
