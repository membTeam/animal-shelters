package ru.animals.collbackCommand;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.animals.service.serviceAPI.DataFromParserCollback;


public abstract class BaseObject {
    public abstract SendMessage apply(CommonCollbackService backServ, Long chartId, DataFromParserCollback dataFromParser);
}
