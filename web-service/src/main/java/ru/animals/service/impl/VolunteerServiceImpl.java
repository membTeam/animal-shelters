package ru.animals.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.animals.entities.Adoption;
import ru.animals.entities.ContentReport;
import ru.animals.entities.commonModel.WebResponseResultVerificationDTO;
import ru.animals.entities.commonModel.WebVerificationResponseDTO;
import ru.animals.entities.enumEntity.EnumAdoptionState;
import ru.animals.entities.enumEntity.EnumStatusReport;
import ru.animals.entities.enumEntity.EnumWebResponseReport;
import ru.animals.models.VolunteerWeb;
import ru.animals.repository.AdoptionalRepository;
import ru.animals.repository.AnimalsRepository;
import ru.animals.repository.ReportsRepository;
import ru.animals.repository.UserBotRepository;
import ru.animals.service.VolunteerService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
public class VolunteerServiceImpl implements VolunteerService {

    private final AdoptionalRepository adoptionalRepository;
    private final UserBotRepository userBotRepository;
    private final AnimalsRepository animalsRepository;
    private final ReportsRepository reportsRepository;


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

        var lsReport = reportsRepository.verificationReport();

        List<WebVerificationResponseDTO> lsResult = new ArrayList<>();
        lsReport.forEach(list-> lsResult.add(new WebVerificationResponseDTO(list)) );

        return lsResult;
    }

    @Override
    public String verificationReport(WebResponseResultVerificationDTO verReport) {

        Long id = verReport.getId();
        LocalDateTime finishDate = reportsRepository.getDateFinishReport(id);
        LocalDateTime dateTimeNow = LocalDateTime.now();

        EnumStatusReport enumStatusReport = EnumWebResponseReport.convertToStatusReport(verReport.getEnumWebResponseReport().getIndex());

        if (finishDate.isBefore(dateTimeNow) && enumStatusReport == EnumStatusReport.REPORT_ACCEPTED )  {
            var optionalContentReport = reportsRepository.findById(id);
            if (optionalContentReport.isEmpty()) {
                return "Отчет не найден";
            }

            ContentReport contentReport = optionalContentReport.get();
            var optionalAdoption = adoptionalRepository.findById(contentReport.getAdoption().getId());

            Adoption adoption = optionalAdoption.get();

            adoption.setAdoptionState(EnumStatusReport.ADOPTED);
            contentReport.setStatusReport(EnumStatusReport.ADOPTED);

            adoptionalRepository.save(adoption);
            reportsRepository.save(contentReport);

            return "Животное усыновлено";
        }



        return "Обработка не реализована";
    }
}
