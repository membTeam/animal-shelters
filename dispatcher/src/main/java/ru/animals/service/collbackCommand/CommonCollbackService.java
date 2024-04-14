package ru.animals.service.collbackCommand;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.animals.utilsDEVL.DataFromParserCollback;
import ru.animals.utilsDEVL.ValueFromMethod;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


@Service
public class CommonCollbackService {

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

    public SendMessage distributeStrCommand(Long chartId, DataFromParserCollback dataFromParser) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {

        /*var param = dataFromParser.getParameter();

        var strCommand = String.format("CollbackComd%s", refactWord(param));
        var pathClass = String.format("%s.impl.%s",
                        this.getClass().getPackageName(), strCommand);

        Class<? extends BaseObject> clazz = (Class<? extends BaseObject>) Class.forName(pathClass);
        Constructor<?> objContr = clazz.getConstructor();*/

        var objPrep = preparationClass(dataFromParser.getParameter());

        if (!objPrep.RESULT) {
            return null;
        }

        /*Class<? extends BaseObject> clazz = (Class<? extends BaseObject>) objPrep.getValue();
        Constructor<?> objContr = clazz.getConstructor();
        BaseObject obj = (BaseObject) objContr.newInstance();*/

        return objPrep.getValue().apply(this, chartId, dataFromParser);

/*      Method method = clazz.getMethod("apply", new Class[] {Long.class, DataFromParserCollback.class});

        method.setAccessible(true);
        SendMessage res = (SendMessage) method.invoke(null, chartId,dataFromParser);*/

//        return res;
    }

}
