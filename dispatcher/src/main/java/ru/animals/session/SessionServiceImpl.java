package ru.animals.session;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.animals.repository.ReportsRepository;
import ru.animals.repository.UserBotRepository;
import ru.animals.session.stateImpl.BaseState;
import ru.animals.session.stateImpl.StateRegister;
import ru.animals.session.stateImpl.StateReport;
import ru.animals.telegramComp.TelgramComp;
import ru.animals.utils.DevlAPI;
import ru.animals.utils.UtilsSendMessage;
import ru.animals.utilsDEVL.ValueFromMethod;
import ru.animals.utilsDEVL.entitiesenum.EnumTypeAppeal;
import ru.animals.utilsDEVL.entitiesenum.EnumTypeParamCollback;
import ru.animals.utilsDEVL.entitiesenum.EnumTypeUpdate;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Getter
@Log4j
public class SessionServiceImpl implements SessionService, SessionUpdate{

    private final UserBotRepository userBotRepository;
    private final ReportsRepository reportsRepository;
    private final UtilsSendMessage utilsSendMessage;

    private Map<Long, BaseState> mapItems = new HashMap<>();

    /*private SendMessage addSessionRegister(long chatId, Update update) {
        StateRegister sessionRegister = new StateRegister(chatId);
        mapItems.put(chatId, sessionRegister);

        return sessionRegister.getSendMessage(this, update);
    }*/

    private ValueFromMethod addSessionReport(long chatId) {

        if (mapItems.containsKey(chatId)) {
            return new ValueFromMethod("Повторное обращение");
        }

        StateReport sessionReport = new StateReport(chatId);
        mapItems.put(chatId, sessionReport);

        return new ValueFromMethod(true, "ok");
    }

    /**
     * Регистрация СОСТОЯНИЯ для регистрации пользователя и отчет о состоянии животного
     * @param chatId
     * @param update
     * @return
     * @throws Exception
     */
    private SendMessage addSessionObject(long chatId, Update update) throws Exception {

        EnumTypeUpdate typeAppeal = DevlAPI.typeUpdate(update, false);

        if (typeAppeal != EnumTypeUpdate.COLLBACK) {
            log.error("addSessionObject тип объекта должен быть COLLBACK");
            return TelgramComp.defaultSendMessage( chatId,"Illegal argument for update");
        }

        var text = DevlAPI.getTextMessFromUpdate(update);

        if (utilsSendMessage.getEnumTypeParameter(text) != EnumTypeParamCollback.TCL_DST) {
            log.error("addSessionObject: EnumTypeParamCollback.TCL_DST");
            return TelgramComp.defaultSendMessage( chatId,"Illegal argument for type update");
        }

        var nameClass = String.format("State%s", DevlAPI.lowercaseFirstLetter(text));
        var pathClass = String.format("%s.stateImpl.%s",
                this.getClass().getPackageName(), nameClass);

        try {
            Class<?> clazz = Class.forName(pathClass);
            Class[] parameter = {long.class};
            Constructor constructor = clazz.getConstructor(parameter);

            BaseState baseState = (BaseState) constructor.newInstance(chatId);
            mapItems.put(chatId, baseState);

            return baseState.getSendMessage(this, update);

        } catch (ClassNotFoundException ex) {
            log.error(ex.getMessage());
            return TelgramComp.defaultSendMessage( chatId,"command not found");
        }

    }

    @Override
    public SendMessage distributionUpdate(Update update) throws Exception {

        ValueFromMethod<Long> valueChatId = DevlAPI.getChatIdFromUpdate(update);

        if (!valueChatId.RESULT) {
            log.error(valueChatId.MESSAGE);
            throw new Exception("Argument is not valid");
        }

        Long chatId =  valueChatId.getValue();

        if (!mapItems.containsKey(chatId)) {
            return addSessionObject(chatId, update);
        }

        return TelgramComp.defaultSendMessage( chatId,"distributionUpdate не завершен");
    }


    @Override
    public UserBotRepository getUserBotRepository() {
        return userBotRepository;
    }

}
