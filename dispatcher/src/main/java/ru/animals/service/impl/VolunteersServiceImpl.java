package ru.animals.service.impl;

import org.springframework.stereotype.Service;
import ru.animals.service.VolunteersService;
import ru.animals.repository.VolunteerRepository;


@Service
public class VolunteersServiceImpl implements VolunteersService {

    private VolunteerRepository volunteersRepository;

    public VolunteersServiceImpl(VolunteerRepository volunteersRepository) {
        this.volunteersRepository = volunteersRepository;
    }

    @Override
    public String contactsVoluteers (){
        var listVolunteer = volunteersRepository.findAll();
        var sb = new StringBuffer();

        listVolunteer.forEach(item -> {
            sb.append(String.format("%s telegram %s телефон %s \n", item.getName(), item.getChartName(), item.getPhone()));
        });

        return sb.toString();
    }

}
