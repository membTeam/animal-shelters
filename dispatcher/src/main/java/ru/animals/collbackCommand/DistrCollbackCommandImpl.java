package ru.animals.collbackCommand;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.animals.collbackCommand.impl.BaseObject;
import ru.animals.repository.UserBotRepository;
import ru.animals.repository.VolunteerRepository;
import ru.animals.utils.parser.StructForCollbackConfig;
import ru.animals.utilsDEVL.ValueFromMethod;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


@Service
public class DistrCollbackCommandImpl implements DistrCollbackCommand{

    private final VolunteerRepository volunteerRepository;
    private final UserBotRepository userBotRepository;

    public DistrCollbackCommandImpl(VolunteerRepository volunteerRepository, UserBotRepository userBotRepository) {
        this.volunteerRepository = volunteerRepository;
        this.userBotRepository = userBotRepository;
    }


    private static String refactWord(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }


    @Override
    public ValueFromMethod<BaseObject> preparationClass(String strClass) {

        var strCommand = String.format("CollbackComd%s", refactWord(strClass));
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

    public SendMessage distributeStrCommand(Long chartId, StructForCollbackConfig dataFromParser) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {

        var objPrep = preparationClass(dataFromParser.getParameter());

        if (!objPrep.RESULT) {
            return null;
        }

        return objPrep.getValue().apply(this, chartId, dataFromParser);

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
