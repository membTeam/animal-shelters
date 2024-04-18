package ru.animals.collbackCommand.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.animals.collbackCommand.DistrCollbackCommand;
import ru.animals.utils.parser.StructForCollbackConfig;


public abstract class BaseObject {
    public abstract SendMessage apply(DistrCollbackCommand repositoryServ, Long chartId, StructForCollbackConfig dataFromParser);
}
