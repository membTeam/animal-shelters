package ru.animals.session.stateImpl.temp;

import ru.animals.controller.UpdateControllerService;
import ru.animals.repository.ReportsRepository;
import ru.animals.repository.UserBotRepository;
import ru.animals.utils.UtilsMessage;
import ru.animals.utils.UtilsSendMessage;

public interface SessionService {
    UserBotRepository getUserBotRepository();
    ReportsRepository getReportsRepository();
    UpdateControllerService getUpdateControllerService();
    UtilsMessage getUtilsMessage();
    UtilsSendMessage getUtilsSendMessage();
}
