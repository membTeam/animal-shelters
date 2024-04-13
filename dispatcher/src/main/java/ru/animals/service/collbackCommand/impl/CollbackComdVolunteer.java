package ru.animals.service.collbackCommand.impl;


import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.animals.service.collbackCommand.BaseObject;
import ru.animals.service.collbackCommand.CommonCollbackService;
import ru.animals.utilsDEVL.DataFromParserCollback;

@NoArgsConstructor
public class CollbackComdVolunteer extends BaseObject {

    @Override
    public SendMessage apply(CommonCollbackService backServ, Long chartId, DataFromParserCollback dataFromParser) {
        var sendMessage = new SendMessage();
        sendMessage.setChatId(chartId);
        sendMessage.setText(" Menod CollbackComdBolunteerю.apply не определен");

        return sendMessage;
    }
}
