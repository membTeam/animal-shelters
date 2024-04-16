package ru.animals.service.impl;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.animals.repository.CommonReposities;
import ru.animals.service.CommonService;
import ru.animals.service.serviceAPI.DataFromParserCollback;


@Service
public class CommonServiceImpl implements CommonService {

    private CommonReposities commonRepo;

    public CommonServiceImpl(CommonReposities comnRepo) {
        this.commonRepo = comnRepo;
    }

    private interface processingMethod{
        SendMessage apply(Long chartId, DataFromParserCollback dataFromParser);
    }

    @Override
    public SendMessage distributeStrCommand(Long chartId, DataFromParserCollback dataFromParser) {

            var param = dataFromParser.getParameter();
            processingMethod method = switch (param) {
                case "dbvolunteers" -> this::contactsVoluteers;
                default -> this::defaultSendMessage;
            };

            return method.apply(chartId, dataFromParser);
    }

    @Override
    public SendMessage defaultSendMessage(Long charId, DataFromParserCollback dataFromParser) {
        var textMessage = "Команда не определена";
        return SendMessage.builder()
                .chatId(charId)
                .text(textMessage)
                .build();
    }

    @Override
    public SendMessage contactsVoluteers(Long chartId, DataFromParserCollback dataFromParser) {
        var collShelters = commonRepo.findAllVolunteer();

        var sb = new StringBuffer();

        collShelters.forEach(item -> {
            sb.append(String.format("%s telegram: %s  телефон: %s \n",
                    item.getName(), item.getChartName(), item.getPhone()));
        });

        return SendMessage
                .builder()
                .chatId(chartId)
                .text(sb.toString())
                .build();
    }
}
