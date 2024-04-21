package ru.animals.session;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.animals.controller.UpdateControllerService;
import ru.animals.entities.UserBot;
import ru.animals.repository.ReportsRepository;
import ru.animals.repository.UserBotRepository;
import ru.animals.session.stateImpl.BaseState;
import ru.animals.session.stateImpl.StateRegister;
import ru.animals.telegramComp.TelgramComp;
import ru.animals.utils.DevlAPI;
import ru.animals.utils.UtilsMessage;
import ru.animals.utils.UtilsSendMessage;
import ru.animals.utilsDEVL.ValueFromMethod;
import ru.animals.utilsDEVL.entitiesenum.EnumTypeParamCollback;
import ru.animals.utilsDEVL.entitiesenum.EnumTypeUpdate;

import java.lang.reflect.Constructor;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Service
@Getter
@Log4j
public class SessionServiceImpl implements SessionService{

    private final UserBotRepository userBotRepository;
    private final ReportsRepository reportsRepository;

    private final UtilsMessage utilsMessage;
    private final UtilsSendMessage utilsSendMessage;


    private Map<Long, BaseState> mapItems = new HashMap<>();

    public SessionServiceImpl(UserBotRepository userBotRepository, ReportsRepository reportsRepository, UtilsMessage utilsMessage, UtilsSendMessage utilsSendMessage) {
        this.userBotRepository = userBotRepository;
        this.reportsRepository = reportsRepository;
        this.utilsMessage = utilsMessage;
        this.utilsSendMessage = utilsSendMessage;
    }

    /**
     * Регистрация СОСТОЯНИЯ для регистрации пользователя и отчет о состоянии животного
     * Class обработки задается в конфигурационном файле: dst-register
     * dst - тип сообщения   register nameClass
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

        var text = update.getCallbackQuery().getData();

        if (utilsSendMessage.getEnumTypeParameter(text) != EnumTypeParamCollback.TCL_DST) {
            log.error("addSessionObject: EnumTypeParamCollback.TCL_DST");
            return TelgramComp.defaultSendMessage( chatId,"Illegal argument for type update");
        }

        text = text.substring(text.indexOf("-") + 1);
        var nameClass = String.format("State%s", DevlAPI.lowercaseFirstLetter(text));

        var pathClass = String.format("%s.stateImpl.%s",
                this.getClass().getPackageName(), nameClass);

        try {
            Class<?> clazz = Class.forName(pathClass);
            Class[] parameter = {Long.class};
            Constructor constructor = clazz.getConstructor(parameter);

            BaseState baseState = (BaseState) constructor.newInstance(chatId);
            mapItems.put(chatId, baseState);

            return baseState.getSendMessage(this, update);

        } catch (ClassNotFoundException ex) {
            log.error(ex.getMessage());
            return TelgramComp.defaultSendMessage( chatId,"command not found");
        }

    }

    public SendMessage distributionUpdate(Update update)  throws Exception {

        ValueFromMethod<Long> valueChatId = DevlAPI.getChatIdFromUpdate(update);

        if (!valueChatId.RESULT) {
            log.error(valueChatId.MESSAGE);
            throw new Exception("Argument is not valid");
        }

        Long chatId =  valueChatId.getValue();

        if (!mapItems.containsKey(chatId)) {
            return addSessionObject(chatId, update);
        }

        var baseState = mapItems.get(chatId);

        var resultSendMessage = baseState.getSendMessage(this, update);

        var resState = baseState.getResultState();
        if (resState.RESULT_END) {
            mapItems.remove(resState.CHAT_ID);
        }

        return resultSendMessage;

    }


    @Override
    public UserBotRepository getUserBotRepository() {
        return userBotRepository;
    }

    @Override
    public ReportsRepository getReportsRepository() {
        return reportsRepository;
    }

    @Override
    public UtilsMessage getUtilsMessage() {
        return utilsMessage;
    }

    @Override
    public UtilsSendMessage getUtilsSendMessage() {
        return utilsSendMessage;
    }

    public boolean isExistsStateSession(Update update) throws Exception {
        ValueFromMethod<Long> valueChatId = DevlAPI.getChatIdFromUpdate(update);
        if (!valueChatId.RESULT) {
            log.error(valueChatId.MESSAGE);
            throw new Exception("The Update object is not defined");
        }

        return mapItems.containsKey(valueChatId.getValue());
    }

}
