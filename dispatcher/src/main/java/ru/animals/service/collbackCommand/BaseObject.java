package ru.animals.service.collbackCommand;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.animals.utilsDEVL.DataFromParserCollback;


public abstract class BaseObject {
    public abstract SendMessage apply(CommonCollbackService backServ, Long chartId, DataFromParserCollback dataFromParser);
}