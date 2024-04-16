package ru.animals.collbackCommand;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.animals.service.serviceParser.DataFromParserCollback;


public abstract class BaseObject {
    public abstract SendMessage apply(CommandServiceRepository repositoryServ, Long chartId, DataFromParserCollback dataFromParser);
}
