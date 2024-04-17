package ru.animals.collbackCommand.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.animals.collbackCommand.BaseObject;
import ru.animals.collbackCommand.CommandServiceRepository;
import ru.animals.utils.parser.StructForCollbackConfig;

public class CollbackComdAllanimalfree extends BaseObject {

    @Override
    public SendMessage apply(CommandServiceRepository repositoryServ, Long chartId, StructForCollbackConfig dataFromParser) {
        var sendMessage = new SendMessage();
        sendMessage.setChatId(chartId);
        sendMessage.setText("Method CollbackComdAllanimalfree.apply не определен");

        return sendMessage;
    }
}
