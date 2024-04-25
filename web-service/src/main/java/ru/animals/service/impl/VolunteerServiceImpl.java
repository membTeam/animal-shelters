package ru.animals.service.impl;

import org.springframework.stereotype.Service;
import ru.animals.entities.Adoption;
import ru.animals.entities.enumEntity.EnumAdoptionState;
import ru.animals.models.VolunteerWeb;
import ru.animals.repository.AdoptionalRepository;
import ru.animals.repository.AnimalsRepository;
import ru.animals.repository.UserBotRepository;
import ru.animals.service.VolunteerService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


@Service
public class VolunteerServiceImpl implements VolunteerService {

    private final AdoptionalRepository adoptionalRepository;
    private final UserBotRepository userBotRepository;
    private final AnimalsRepository animalsRepository;

    public VolunteerServiceImpl(AdoptionalRepository adoptionalRepository, UserBotRepository userBotRepository, AnimalsRepository animalsRepository) {
        this.adoptionalRepository = adoptionalRepository;
        this.userBotRepository = userBotRepository;
        this.animalsRepository = animalsRepository;
    }


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
}
