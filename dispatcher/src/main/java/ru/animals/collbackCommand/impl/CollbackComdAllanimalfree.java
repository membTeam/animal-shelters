package ru.animals.collbackCommand.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.animals.collbackCommand.DistrCollbackCommand;
import ru.animals.utils.parser.StructForCollbackConfig;

public class CollbackComdAllanimalfree extends BaseObject {

    @Override
    public SendMessage apply(DistrCollbackCommand repositoryServ, Long chartId, StructForCollbackConfig dataFromParser) {
        var animationRepo = repositoryServ.getAnimalsRepository();

        var sendMessage = new SendMessage();
        sendMessage.setChatId(chartId);
        sendMessage.setText("Используйте адрес в браузере:\n" +
                "/web-animal/list-animals");

        return sendMessage;
    }
}
