package ru.animals.collbackCommand.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.animals.collbackCommand.BaseObject;
import ru.animals.utilsDEVL.DataFromParserCollback;

public class CommonCollbackService extends BaseObject {
    @Override
    public SendMessage apply(ru.animals.collbackCommand.CommonCollbackService backServ, Long chartId, DataFromParserCollback dataFromParser) {
        var sendMessage = new SendMessage();
        sendMessage.setChatId(chartId);
        sendMessage.setText("Method CommonCollbackService.apply не определен");

        return sendMessage;
    }
}
