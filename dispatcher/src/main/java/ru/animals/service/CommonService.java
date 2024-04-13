package ru.animals.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.animals.utilsDEVL.DataFromParserCollback;

public interface CommonService {

    SendMessage distributeStrCommand(Long chartId, DataFromParserCollback dataFromParser);

    SendMessage defaultSendMessage(Long charId, DataFromParserCollback dataFromParser);

    SendMessage contactsVoluteers(Long chartId, DataFromParserCollback dataFromParser);

}
