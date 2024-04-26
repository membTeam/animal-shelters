package ru.animals.utils.filePhoto;

import ru.animals.entities.Adoption;
import ru.animals.entities.ContentReport;
import ru.animals.entities.UserBot;
import ru.animals.entities.enumEntity.EnumStatusReport;
import ru.animals.repository.AdoptionalRepository;
import ru.animals.repository.ReportsRepository;
import ru.animals.repository.UserBotRepository;
import ru.animals.session.SessionService;
import ru.animals.session.StateReportServ;
import ru.animals.session.stateImpl.DataBufferReport;

import java.time.LocalDate;
import java.util.List;

public class FilePhotoAPI {

    public static FilePhotoDTO preparationContentRepository(SessionService sessionService, StateReportServ reportServ ) {
        FilePhotoDTO result = new FilePhotoDTO();

        DataBufferReport buferReport = reportServ.getDataBufferReport();
        UserBotRepository userRepository = sessionService.getUserBotRepository();
        AdoptionalRepository adoptionalRepo = sessionService.getAdoptionalRepository();

        var optUserBot = userRepository.findByChatId(buferReport.getChatId());
        if (optUserBot.isEmpty()) {
            return initWithError("Пользователь не найден");
        }

        UserBot userBot = optUserBot.get();

        List<Adoption> lsContentReport = adoptionalRepo
                .findByUserIdForONPROBATION(userBot.getId());
        if (lsContentReport.size() == 0) {
            return initWithError("Нет данных по усыновлению");
        }

        ContentReport contentReport = ContentReport.builder()
                .date(LocalDate.now())
                .adoptionId(lsContentReport.get(0).getId())
                .statusReport(EnumStatusReport.RECEIPT_REPORT)
                .changeBehavior(buferReport.getChangeInBehavior())
                .animalDiet(buferReport.getAnimalDiet())
                .generalWellBeing(buferReport.getGeneralWellBeing())
                .statusReport(EnumStatusReport.RECEIPT_REPORT)
                .metaDataPhoto(null)
                .build();

        result.setContentReport(contentReport);
        result.setUserBot(userBot);

        return result;

        /*
        private Long adoptionId;
        private LocalDate date;

        private String animalDiet;
        private String changeBehavior;
        private String generalWellBeing;

        private EnumStatusReport statusReport;

        private MetaDataPhoto metaDataPhoto;
         */

    }

    private static FilePhotoDTO initWithError(String mes) {
        var result = new FilePhotoDTO();
        result.setError(mes);

        return result;
    }

}
