package ru.animals.service.imp;


import org.springframework.stereotype.Service;
import ru.animals.entities.UserBot;
import ru.animals.entities.enumEntity.EnumRoleUser;
import ru.animals.entities.enumEntity.EnumState;
import ru.animals.repository.UserBotRepository;
import ru.animals.service.UserBotService;

@Service
public class UserBotServiceImpl implements UserBotService {

    private UserBotRepository userBotRepository;

    public UserBotServiceImpl(UserBotRepository userBotRepository) {
        this.userBotRepository = userBotRepository;
    }

    @Override
    public UserBot addUserBot(Long chatId, String firstName, EnumRoleUser role, EnumState state) {
        return null;
    }

}
