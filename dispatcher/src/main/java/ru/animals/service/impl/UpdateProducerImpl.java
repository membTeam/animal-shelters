package ru.animals.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.animals.service.UpdateProducer;
import ru.animals.utils.UtilsSendMessage;

@Service
@Log4j
public class UpdateProducerImpl implements UpdateProducer {
    private RabbitTemplate rabbitTemplate;
    private UtilsSendMessage parsingCommand;

    public UpdateProducerImpl(RabbitTemplate rabbitTemplate, UtilsSendMessage parsingCommand) {
        this.rabbitTemplate = rabbitTemplate;
        this.parsingCommand = parsingCommand;
    }

    @Override
    public void produce(String rabbitQueue, Update update) {
        log.debug(update.getMessage().getText());
        rabbitTemplate.convertAndSend(rabbitQueue, update);
    }
}
