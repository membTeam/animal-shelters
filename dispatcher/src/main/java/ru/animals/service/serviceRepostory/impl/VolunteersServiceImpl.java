package ru.animals.service.serviceRepostory.impl;

import org.springframework.stereotype.Service;
import ru.animals.repository.VolunteersRepository;
import ru.animals.service.serviceRepostory.VolunteersService;


@Service
public class VolunteersServiceImpl implements VolunteersService {

    private VolunteersRepository volunteersRepository;

    public VolunteersServiceImpl(VolunteersRepository volunteersRepository) {
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
