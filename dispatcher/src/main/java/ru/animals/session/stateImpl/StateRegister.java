package ru.animals.session.stateImpl;

import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.animals.entities.UserBot;
import ru.animals.session.SessionService;
import ru.animals.telegramComp.TelgramComp;
import ru.animals.utils.DevlAPI;
import ru.animals.utilsDEVL.entitiesenum.EnumTypeParamCollback;
import ru.animals.utilsDEVL.entitiesenum.EnumTypeUpdate;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Log4j
public class StateRegister extends BaseState {

    private final Long charId;
    private LocalDateTime lastAppeal;
    private boolean resultEnd = false;
    private boolean startState = false;
    private final String STR_PATTERN ="^(\\(\\+7\\)) (\\(9\\d{2}\\)) (\\(\\d{3}\\)) (\\(\\d{2}-\\d{2}\\))$";

    public final String TEXT_START = "Введите свои данные согласно шаблона:\n" +
                            "ВашеИмя (+7) (9NN) (NNN) (NN-NN)\n" +
                            "где N числовые значения вашего телефона\n" +
                            "отказ от регистрации /cancel";

    public StateRegister(long chatId) {
        this.charId = chatId;
        this.lastAppeal = LocalDateTime.now();
    }

    private SendMessage registerState(EnumTypeUpdate enumTypeUpdate, String text) {
        if ( enumTypeUpdate == EnumTypeUpdate.COLLBACK
                && EnumTypeParamCollback.enumByString(text) == EnumTypeParamCollback.TCL_DST ) {

            startState = true;
            return TelgramComp.defaultSendMessage(charId, TEXT_START);

        } else {
            log.error("StateRegister тип не определен");
            return TelgramComp.defaultSendMessage(charId,"type was not find /start");
        }
    }

    /**
     * формат входящего сообщения YourName (+7)-(9**)-(NNN)-(NN-NN)
     * @param update
     * @return
     */
    @Override
    public SendMessage getSendMessage(SessionService sessionService, Update update) {

        var enumTypeUpdate = DevlAPI.typeUpdate(update, false);
        var text = DevlAPI.getTextMessFromUpdate(update);

        if (!startState) {
            return registerState(enumTypeUpdate, text);
        }

        if (enumTypeUpdate != EnumTypeUpdate.TEXT_MESSAGE) {
            log.error("StateRegister: не соответствие типа объекта update");
            return TelgramComp.defaultSendMessage(charId,"internal type error /start");
        }

        var index = text.indexOf("(");
        var userName = text.substring(0, index).trim();
        var strPhone = text.substring(index);
        var pattern = Pattern.compile(STR_PATTERN);

        Matcher matcher = pattern.matcher(strPhone);
        if (matcher.find()) {
            var userRepository = sessionService.getUserBotRepository();
            var strPhone = String.format("%s %s %s %s",
                    matcher.group(1), matcher.group(2),
                    matcher.group(3), matcher.group(4) );

            //String userName = update.getMessage().getChat().getFirstName();

            UserBot userBot = UserBot.builder()
                    .chatId(charId)
                    .phone(strPhone)
                    .firstName(userName)
                    .build();

            userRepository.save(userBot);
            resultEnd = true;
            
            TelgramComp.defaultSendMessage( charId, "Регистрация прошла успешно.\nПерейдите в стартовое меню /start");
        }

    }
}
