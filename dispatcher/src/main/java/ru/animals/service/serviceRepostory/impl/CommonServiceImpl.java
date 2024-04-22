package ru.animals.service.serviceRepostory.impl;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.animals.repository.CommonReposities;
import ru.animals.service.serviceRepostory.CommonService;
import ru.animals.utils.parser.StructForCollbackConfig;


@Service
public class CommonServiceImpl implements CommonService {

    private CommonReposities commonRepo;

    public CommonServiceImpl(CommonReposities comnRepo) {
        this.commonRepo = comnRepo;
    }

    private interface processingMethod{
        SendMessage apply(Long chartId, StructForCollbackConfig dataFromParser);
    }

    @Override
    public SendMessage distributeStrCommand(Long chartId, StructForCollbackConfig dataFromParser) {

            var param = dataFromParser.getParameter();
            processingMethod method = switch (param) {
                case "dbvolunteers" -> this::contactsVoluteers;
                default -> this::defaultSendMessage;
            };

            return method.apply(chartId, dataFromParser);
    }

    @Override
    public SendMessage defaultSendMessage(Long charId, StructForCollbackConfig dataFromParser) {
        var textMessage = "Команда не определена";
        return SendMessage.builder()
                .chatId(charId)
                .text(textMessage)
                .build();
    }

    @Override
    public SendMessage contactsVoluteers(Long chartId, StructForCollbackConfig dataFromParser) {
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
