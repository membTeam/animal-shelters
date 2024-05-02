package ru.animals.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.animals.entities.Logmessage;
import ru.animals.entities.UserBot;
import ru.animals.repository.CommonReposities;
import ru.animals.repository.LogmessageRepository;
import ru.animals.repository.UserBotRepository;
import ru.animals.service.CommonService;
import ru.animals.utils.UtilsMessage;
import ru.animals.utils.parser.StructForCollbackConfig;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CommonServiceImpl implements CommonService {

    private final CommonReposities commonRepo;
    private final UserBotRepository userBotRepository;
    private final LogmessageRepository logmessageRepository;
    private final UtilsMessage utilsMessage;

    private interface processingMethod{
        SendMessage apply(Long chartId, StructForCollbackConfig dataFromParser);
    }

    /**
     * Сообщения от волонтеров
     * @return null or SendMessage()
     */
    @Override
    public List<SendMessage> messageForUser(Long chartId) {

        UserBot userBot = userBotRepository.findByChatId(chartId).orElseThrow();
        List<Logmessage> lsLog = logmessageRepository.findAllByChatId(chartId);

        if (lsLog.size() > 0) {

            var strChatId = String.valueOf(chartId);

            var result = lsLog.stream().map(item -> {
                        SendMessage localResult;
                        var typeconfigCommand = item.getTypeConfCommand();

                        if ( typeconfigCommand.getIndex() > 1 ) {
                            localResult = utilsMessage.generaleSendMessageCongratulation(chartId, typeconfigCommand);
                        } else {
                            localResult = new SendMessage(strChatId, item.getMessage());
                        }

                        return localResult;
                    }
            ).toList();

            logmessageRepository.deleteAll(lsLog);
            return  result;
        } else {
            return null;
        }

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
