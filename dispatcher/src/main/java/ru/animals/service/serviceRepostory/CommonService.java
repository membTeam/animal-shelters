package ru.animals.service.serviceRepostory;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.animals.service.serviceParser.DataFromParserCollback;

public interface CommonService {

    SendMessage distributeStrCommand(Long chartId, DataFromParserCollback dataFromParser);

    SendMessage defaultSendMessage(Long charId, DataFromParserCollback dataFromParser);

    SendMessage contactsVoluteers(Long chartId, DataFromParserCollback dataFromParser);

}
