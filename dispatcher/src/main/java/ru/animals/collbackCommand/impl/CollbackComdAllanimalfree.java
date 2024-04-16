package ru.animals.collbackCommand.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.animals.collbackCommand.BaseObject;
import ru.animals.collbackCommand.CommandServiceRepository;
import ru.animals.collbackCommand.CommonCollbackService;
import ru.animals.service.serviceParser.DataFromParserCollback;

public class CollbackComdAllanimalfree extends BaseObject {

    @Override
    public SendMessage apply(CommandServiceRepository repositoryServ, Long chartId, DataFromParserCollback dataFromParser) {
        var sendMessage = new SendMessage();
        sendMessage.setChatId(chartId);
        sendMessage.setText("Method CollbackComdAllanimalfree.apply не определен");

        return sendMessage;
    }
}
