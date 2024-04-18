package ru.animals.session;

import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.animals.repository.UserBotRepository;
import ru.animals.session.stateImpl.BaseState;
import ru.animals.session.stateImpl.StateRegister;
import ru.animals.session.stateImpl.StateReport;
import ru.animals.utils.DevlAPI;
import ru.animals.utilsDEVL.ValueFromMethod;
import ru.animals.utilsDEVL.entitiesenum.EnumTypeAppeal;

import java.util.HashMap;
import java.util.Map;

@Service
@Getter
@Log4j
public class SessionServiceImpl implements SessionService{

    private final UserBotRepository userBotRepository;

    private Map<Long, BaseState> mapItems = new HashMap<>();

    public SessionServiceImpl(UserBotRepository userBotRepository) {
        this.userBotRepository = userBotRepository;
    }

/*    private ValueFromMethod getChatIdFromUpdate(Update update) {

        if (update == null || (!update.hasMessage() || !update.hasCallbackQuery())) {
            return new ValueFromMethod(false, "update не определен");
        }

        long chatId = update.hasMessage()
                ? update.getMessage().getChatId()
                : update.getCallbackQuery().getMessage().getChatId();

        return new ValueFromMethod<Long>(chatId);
    }*/

    private ValueFromMethod addSessionRegister(long chatId) {
        if (mapItems.containsKey(chatId)) {
            return new ValueFromMethod("Повторное обращение");
        }

        StateRegister sessionRegister = new StateRegister(chatId);
        mapItems.put(chatId, sessionRegister);

        return new ValueFromMethod(true, "ok");
    }

    private ValueFromMethod addSessionReport(long chatId) {

        if (mapItems.containsKey(chatId)) {
            return new ValueFromMethod("Повторное обращение");
        }

        StateReport sessionReport = new StateReport(chatId);
        mapItems.put(chatId, sessionReport);

        return new ValueFromMethod(true, "ok");
    }

    private ValueFromMethod addSessionObject(Update update, EnumTypeAppeal typeAppeal) {

        ValueFromMethod<Long> valueChatId = DevlAPI.getChatIdFromUpdate(update);

        if (!valueChatId.RESULT) {
            return new ValueFromMethod(false, "chatId не определен");
        }

        Long chatid = valueChatId.getValue();

        if (typeAppeal == EnumTypeAppeal.REGUSTER_USER) {
            return addSessionRegister(chatid);
        } else {
            return addSessionReport(chatid);
        }

    }

    public SendMessage distributionUpdate(Update update) throws Exception {

        ValueFromMethod<Long> valueChatId = DevlAPI.getChatIdFromUpdate(update);

        if (!valueChatId.RESULT) {
            log.error(valueChatId.MESSAGE);
            throw new Exception("Argument is not valid");
        }

        Long chatId =  valueChatId.getValue();

        if (!mapItems.containsKey(chatId)) {

        }

        return null;
    }


    @Override
    public UserBotRepository getUserBotRepository() {
        return userBotRepository;
    }

}
