package ru.animals.session;

import ru.animals.controller.UpdateControllerService;
import ru.animals.repository.ReportsRepository;
import ru.animals.repository.UserBotRepository;
import ru.animals.utils.UtilsMessage;
import ru.animals.utils.UtilsSendMessage;

public interface SessionService {
    UserBotRepository getUserBotRepository();
    ReportsRepository getReportsRepository();

    UtilsMessage getUtilsMessage();
    UtilsSendMessage getUtilsSendMessage();
}
