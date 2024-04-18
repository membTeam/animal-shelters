package ru.animals.collbackCommand.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.animals.collbackCommand.DistrCollbackCommand;
import ru.animals.utils.parser.StructForCollbackConfig;

public class CollbackComdinfofreecat extends BaseObject{
    @Override
    public SendMessage apply(DistrCollbackCommand repositoryServ, Long chartId, StructForCollbackConfig dataFromParser) {
        var sendMessage = new SendMessage();
        sendMessage.setChatId(chartId);
        sendMessage.setText("Method CollbackComdinfofreecat.apply не определен");

        return sendMessage;
    }
}
