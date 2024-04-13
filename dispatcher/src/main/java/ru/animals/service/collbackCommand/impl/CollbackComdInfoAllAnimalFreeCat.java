package ru.animals.service.collbackCommand.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.animals.service.collbackCommand.BaseObject;
import ru.animals.service.collbackCommand.CommonCollbackService;
import ru.animals.utilsDEVL.DataFromParserCollback;

public class CollbackComdInfoAllAnimalFreeCat extends BaseObject {

    @Override
    public SendMessage apply(CommonCollbackService backServ, Long chartId, DataFromParserCollback dataFromParser) {
        var sendMessage = new SendMessage();
        sendMessage.setChatId(chartId);
        sendMessage.setText("Method CollbackComdInfoAllAnimalFreeCat.apply не определен");

        return sendMessage;
    }
}
