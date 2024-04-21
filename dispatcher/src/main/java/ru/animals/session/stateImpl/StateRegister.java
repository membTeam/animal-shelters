package ru.animals.session.stateImpl;

import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.animals.entities.UserBot;
import ru.animals.session.SessionServiceImpl;
import ru.animals.telegramComp.TelgramComp;
import ru.animals.utils.DevlAPI;
import ru.animals.utilsDEVL.entitiesenum.EnumTypeUpdate;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Log4j
public class StateRegister extends BaseState {
    @Getter
    private final Long chatId;

    @Getter
    private LocalDateTime dateTimeLastAppeal;

    @Getter
    private boolean resultEnd = false;

    @Getter
    private boolean startState = false;

    private final String STR_PATTERN = "^(\\(\\+7\\)) (\\(9\\d{2}\\)) (\\(\\d{3}\\)) (\\(\\d{2}-\\d{2}\\))$";


    public final String TEXT_START = "Введите свои данные согласно шаблона:\n" +
            "ВашеИмя (+7) (9NN) (NNN) (NN-NN)\n" +
            "где N числовые значения вашего телефона\n" +
            "отказ от регистрации /cancel";

    public StateRegister(Long chatId) {
        this.chatId = chatId;
        this.dateTimeLastAppeal = LocalDateTime.now();

    }

    private SendMessage registerState() {

            startState = true;
            return SendMessage.builder()
                    .chatId(chatId)
                    .text(TEXT_START)
                    .build();
    }

    /**
     * формат входящего сообщения YourName (+7)-(9**)-(NNN)-(NN-NN)
     *
     * @param update
     * @return
     */
    @Override
    public SendMessage getSendMessage(SessionServiceImpl sessionService, Update update) {

        var enumTypeUpdate = DevlAPI.typeUpdate(update, false);

        if (!startState) {
            return registerState();
        }

        if (enumTypeUpdate != EnumTypeUpdate.TEXT_MESSAGE ) {
            log.error("StateRegister: не соответствие типа объекта update");
            return TelgramComp.defaultSendMessage(chatId, "internal type error /start");
        }

        dateTimeLastAppeal = LocalDateTime.now();

        var message = update.getMessage();

        var text = message.getText();
        var index = text.indexOf("(");
        var userName = text.substring(0, index).trim();
        var strPhone = text.substring(index);
        var pattern = Pattern.compile(STR_PATTERN);

        Matcher matcher = pattern.matcher(strPhone);
        SendMessage sendMessageResult;
        String strPhoneFormat;

        UserBot userBot = null;

        if (matcher.find()) {
            strPhoneFormat = String.format("%s %s %s %s",
                    matcher.group(1), matcher.group(2),
                    matcher.group(3), matcher.group(4));

            userBot = new UserBot();
            userBot.setDateCreate(LocalDateTime.now());
            userBot.setDateUpdate(LocalDateTime.now());
            userBot.setChatId(chatId);
            userBot.setPhone(strPhoneFormat);
            userBot.setFirstName(userName);
            userBot.setLastName(message.getChat().getLastName());

        } else {
            return sendMessageResult = TelgramComp.defaultSendMessage(chatId, "illegal telephone number");
        }

        try {

            var userRepository = sessionService.getUserBotRepository();

            userRepository.save(userBot);
            sendMessageResult = TelgramComp.defaultSendMessage(chatId, "Регистрация прошла успешно.\nПерейдите в стартовое меню /start");

            resultEnd = true;

        } catch (Exception ex) {
            log.error(ex.getMessage());
            sendMessageResult = TelgramComp.defaultSendMessage(chatId, "The server rejected the registration");
        }

        return sendMessageResult;

    }

    @Override
    public ResultState getResultState() {
        return ResultState.builder()
                .START_STATE(startState)
                .RESULT_END(resultEnd)
                .CHAT_ID(chatId)
                .LAST_APPEAL(dateTimeLastAppeal)
                .build();

    }

}
