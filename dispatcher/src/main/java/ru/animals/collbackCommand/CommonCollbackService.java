package ru.animals.collbackCommand;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.animals.repository.VolunteerRepository;
import ru.animals.utils.parser.StructForCollbackConfig;
import ru.animals.utilsDEVL.ValueFromMethod;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


@Service
public class CommonCollbackService implements CommandServiceRepository {

    private VolunteerRepository volunteerRepository;

    public CommonCollbackService(VolunteerRepository volunteerRepository) {
        this.volunteerRepository = volunteerRepository;
    }

    private static String refactWord(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }

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
}
