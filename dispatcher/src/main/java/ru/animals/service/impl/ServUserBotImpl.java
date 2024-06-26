package ru.animals.service.impl;

import org.springframework.stereotype.Service;
import ru.animals.entities.Adoption;
import ru.animals.entities.UserBot;
import ru.animals.entities.enumEntity.EnumAdoptionState;
import ru.animals.repository.AdoptionalRepository;
import ru.animals.repository.UserBotRepository;
import ru.animals.service.enumStatusUser.EnumStatusUserBot;
import ru.animals.service.ServUserBot;

import java.util.Collection;


@Service
public class ServUserBotImpl implements ServUserBot {

    private UserBotRepository userRepository;
    private AdoptionalRepository adoptionalRepository;

    public ServUserBotImpl(UserBotRepository userRepository, AdoptionalRepository adoptionalRepository) {
        this.userRepository = userRepository;
        this.adoptionalRepository = adoptionalRepository;
    }


    @Override
    public EnumStatusUserBot statudUserBot(long chatId) {

        var optionalRes = userRepository.findByChatId(chatId);

        if (optionalRes.isEmpty()) {
            return EnumStatusUserBot.USER_NOT_REGISTER;
        }

        UserBot userBot = optionalRes.get();
        Collection<Adoption> collectionAdoption =
                adoptionalRepository.findByUserIdAndOnProbational(userBot.getId(), EnumAdoptionState.ON_PROBATION);

        return collectionAdoption.size() > 0
                ? EnumStatusUserBot.ON_PROBATION_PERIOD
                : EnumStatusUserBot.NO_PROBATION_PERIOD;

    }

}
