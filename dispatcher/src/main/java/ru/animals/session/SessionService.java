package ru.animals.session;

import ru.animals.controller.TelegramBot;
import ru.animals.repository.AdoptionalRepository;
import ru.animals.repository.BreedsRepository;
import ru.animals.repository.ReportsRepository;
import ru.animals.repository.UserBotRepository;
import ru.animals.utils.UtilsMessage;
import ru.animals.utils.UtilsSendMessage;

public interface SessionService {
    UserBotRepository getUserBotRepository();
    ReportsRepository getReportsRepository();
    AdoptionalRepository getAdoptionalRepository();
    String getImageStorageDirReport();

    TelegramBot getTelegramBot();


    BreedsRepository getBreedsRepository();
}
