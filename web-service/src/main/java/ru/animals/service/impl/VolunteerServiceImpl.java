package ru.animals.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.animals.entities.Adoption;
import ru.animals.entities.ContentReport;
import ru.animals.entities.Logmessage;
import ru.animals.entities.UserBot;
import ru.animals.entities.commonModel.WebResponseResultVerificationDTO;
import ru.animals.entities.commonModel.WebVerificationResponseDTO;
import ru.animals.entities.enumEntity.EnumAdoptionState;
import ru.animals.entities.enumEntity.EnumStatusReport;
import ru.animals.entities.enumEntity.EnumWebResponseReport;
import ru.animals.exeption.WebException;
import ru.animals.models.VolunteerWeb;
import ru.animals.models.WebResponseOkOrNo;
import ru.animals.models.serviceWebModels.WebAnimalsResponseServ;
import ru.animals.repository.*;
import ru.animals.service.VolunteerService;
import ru.animals.utilsDEVL.entitiesenum.EnumTypeConfCommand;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;



@Log4j
@Service
@RequiredArgsConstructor
public class VolunteerServiceImpl implements VolunteerService {
    private final LogmessageRepository logMessage;

    private final AdoptionalRepository adoptionalRepository;
    private final UserBotRepository userBotRepository;
    private final AnimalsRepository animalsRepository;
    private final ReportsRepository reportsRepository;
    private final LogmessageRepository logmessageRepository;


    @Override
    public String adoption(VolunteerWeb volunteerWeb) {

        if (userBotRepository.findById(volunteerWeb.getUserId()).isEmpty() ||
                animalsRepository.findById(volunteerWeb.getAnimalsIid()).isEmpty()) {
            return "ОШибка ввода данных. Проверьте параметры: userId and ainmalsId";
        }

        boolean verifyExists = adoptionalRepository.findByUserIdAndAnimalsId(volunteerWeb.getUserId(), volunteerWeb.getAnimalsIid());
        if (verifyExists) {
            return "Повторный ввод данных";
        }

        LocalDateTime localDateStart = LocalDateTime.now();
        var localDateEnd = localDateStart.plusDays(30);

        Date dateStart = Date.from(localDateStart.atZone( ZoneId.systemDefault()).toInstant());
        Date dateEnd = Date.from(localDateEnd.atZone( ZoneId.systemDefault()).toInstant());

        try {
            Adoption adoption = Adoption.builder()
                    .userId(volunteerWeb.getUserId())
                    .animalsIid(volunteerWeb.getAnimalsIid())
                    .adoptionState(EnumAdoptionState.ON_PROBATION)
                    .dateStart(dateStart)
                    .dateFinish(dateEnd)
                    .build();
            adoptionalRepository.save(adoption);

            return "Registration was successful";
        } catch (Exception ex) {
            return "the server rejected the processing";
        }
    }

    @Override
    public List<WebVerificationResponseDTO> getReprotForVerification() {

        return WebAnimalsResponseServ.getListWebVerification(reportsRepository);
    }

    @Override
    @Transactional
    public WebResponseOkOrNo verificationReport(Long id, WebResponseResultVerificationDTO verReport) {

        WebResponseOkOrNo result;
        EnumTypeConfCommand enumTypeConfCommand = null;
        try {

            if (verReport.getEnumWebResponseReport() == null || verReport.getMessage().isBlank()) {
                throw new WebException("Проверьте заполнение полей");
            }

            String message = verReport.getMessage();

            LocalDateTime finishDate = reportsRepository.getDateFinishReport(id);
            if (finishDate == null) {
                throw new WebException("Нет данных по усыновлению");
            }

            LocalDateTime dateTimeNow = LocalDateTime.now();

            EnumStatusReport enumStatusReport = EnumWebResponseReport.convertToEnumStatusReport(verReport.getEnumWebResponseReport().getIndex());
            if (enumStatusReport == EnumStatusReport.NONE) {
                throw new IllegalArgumentException("Перечисление не определено");
            }

            ContentReport contentReport = reportsRepository.findById(id).orElseThrow();
            Long chatId = reportsRepository.getChatId(contentReport.getId());
            UserBot userBot = userBotRepository.findByChatId(chatId).orElseThrow();

            if (finishDate.isBefore(dateTimeNow) && enumStatusReport == EnumStatusReport.REPORT_ACCEPTED) {

                Long adoptionId = contentReport.getAdoption().getId();
                Adoption adoption = adoptionalRepository.findById(adoptionId).orElseThrow();

                adoption.setAdoptionState(EnumAdoptionState.ADOPTED);
                contentReport.setStatusReport(EnumStatusReport.ADOPTED);

                enumTypeConfCommand = EnumTypeConfCommand.FILE_CONGRATULATION_ADOPTION;

                adoptionalRepository.save(adoption);
                reportsRepository.save(contentReport);

            } else {
                contentReport.setStatusReport(enumStatusReport);
                enumTypeConfCommand = EnumTypeConfCommand.TEXT;
                reportsRepository.save(contentReport);
            }

            result = enumStatusReport.getIndex() >= 100
                    ? new WebResponseOkOrNo(true, message)
                    : new WebResponseOkOrNo(false, message);

            Logmessage logmessage = Logmessage.builder()
                    .dateMessage(LocalDateTime.now())
                    .chatId(userBot.getId())
                    .message(message)
                    .typeConfCommand(enumTypeConfCommand)
                    .build();

            logmessageRepository.save(logmessage);

        } catch (Exception ex) {
            log.error("verificationReport: \n" + ex.getMessage());

            if (ex instanceof WebException) {
                result = new WebResponseOkOrNo(false, ex.getMessage());
            } else {
                result = new WebResponseOkOrNo(false, "Сервер отклонил обработку");
            }
        }

        return result;
    }
}
