package ru.animals.collbackCommand;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.animals.collbackCommand.impl.BaseObject;
import ru.animals.repository.AnimalsRepository;
import ru.animals.repository.UserBotRepository;
import ru.animals.repository.VolunteerRepository;
import ru.animals.telegramComp.TelgramComp;
import ru.animals.utils.DevlAPI;
import ru.animals.utils.parser.StructForCollbackConfig;
import ru.animals.utilsDEVL.ValueFromMethod;

import java.lang.reflect.Constructor;


@Service
@Log4j
@RequiredArgsConstructor
public class DistrCollbackCommandImpl implements DistrCollbackCommand{

    private final VolunteerRepository volunteerRepository;
    private final UserBotRepository userBotRepository;
    private final AnimalsRepository animalsRepository;


    @Override
    public ValueFromMethod<BaseObject> preparationClass(String strClass) {

        var strCommand = String.format("CollbackComd%s", DevlAPI.lowercaseFirstLetter(strClass));
        var pathClass = String.format("%s.impl.%s",
                this.getClass().getPackageName(), strCommand);

        try {
            Class<? extends BaseObject> clazz = (Class<? extends BaseObject>) Class.forName(pathClass);
            Constructor<?> objContr = clazz.getConstructor();
            BaseObject obj = (BaseObject) objContr.newInstance();

            return new ValueFromMethod<BaseObject>(obj);

        } catch (Exception e) {
            return new ValueFromMethod(e.getMessage());
        }
    }

    public SendMessage distributeStrCommand(Long chartId, StructForCollbackConfig dataConfigStruct) throws Exception {

        var objPrep = preparationClass(dataConfigStruct.getParameter());

        if (!objPrep.RESULT) {
            log.error("DistrCollbackCommandImpl: " + objPrep.MESSAGE);
            return TelgramComp.defaultSendMessage(chartId, "Метод не определен");
        }

        return objPrep.getValue().apply(this, chartId, dataConfigStruct);
    }

    @Override
    public AnimalsRepository getAnimalsRepository() {
        return animalsRepository;
    }

    @Override
    public VolunteerRepository getVolunteerRepository() {
        return volunteerRepository;
    }

    @Override
    public UserBotRepository getUserBotRepository() {
        return userBotRepository;
    }
}
