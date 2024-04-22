package ru.animals.collbackCommand.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.animals.collbackCommand.DistrCollbackCommand;
import ru.animals.utils.parser.StructForCollbackConfig;

public class CollbackComdVolunteer extends BaseObject{
    @Override
    public SendMessage apply(DistrCollbackCommand repositoryServ, Long chartId, StructForCollbackConfig dataFromParser) {
        StringBuilder sb = new StringBuilder();

        var volunteerRepo = repositoryServ.getVolunteerRepository();
        var sendMessage = new SendMessage();
        sendMessage.setChatId(chartId);

        var lsVolunteer = volunteerRepo.findAll();
        if (lsVolunteer.size() == 0) {
            sendMessage.setText("Нет данных по полонтерам");
            return sendMessage;
        }

        lsVolunteer.forEach(item-> {
            var str = String.format("chat:%s  name:%s phone:%s\n",
                    item.getChartName(), item.getName(), item.getPhone());
            sb.append(str);
        });

        sendMessage.setText(sb.toString());

        return sendMessage;
    }
}
