package ru.animals.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.animals.utils.parser.StructForCollbackConfig;

import java.util.List;

public interface CommonService {

    List<SendMessage> messageForUser(Long chartId);

    SendMessage distributeStrCommand(Long chartId, StructForCollbackConfig dataFromParser);

    SendMessage defaultSendMessage(Long charId, StructForCollbackConfig dataFromParser);

    SendMessage contactsVoluteers(Long chartId, StructForCollbackConfig dataFromParser);

}
