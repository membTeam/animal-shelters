package ru.animals.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.animals.utils.parser.StructForCollbackConfig;

public interface CommonService {

    SendMessage distributeStrCommand(Long chartId, StructForCollbackConfig dataFromParser);

    SendMessage defaultSendMessage(Long charId, StructForCollbackConfig dataFromParser);

    SendMessage contactsVoluteers(Long chartId, StructForCollbackConfig dataFromParser);

}
