package ru.animals.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.animals.utilsDEVL.DataFromParser;

import java.util.Collection;

public interface CommonService {

    SendMessage distributeStrCommand(Long chartId, DataFromParser dataFromParser);

    SendMessage defaultSendMessage(Long charId, DataFromParser dataFromParser);

    SendMessage contactsVoluteers(Long chartId, DataFromParser dataFromParser);

}
