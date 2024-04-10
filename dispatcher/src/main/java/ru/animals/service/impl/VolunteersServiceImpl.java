package ru.animals.service.impl;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import ru.animals.entities.Breeds;
import ru.animals.entities.Volunteers;
import ru.animals.repository.VolunteersRepository;
import ru.animals.service.VolunteersService;

import java.util.Collection;


@Service
public class VolunteersServiceImpl implements VolunteersService {

    private VolunteersRepository volunteersRepository;

    public VolunteersServiceImpl(VolunteersRepository volunteersRepository) {
        this.volunteersRepository = volunteersRepository;
    }

    /*@Override
    public Collection<Volunteers> allDataBreeds() {
        var result = volunteersRepository.volonterForChartName("chartSecornd");
        return result;

    }*/

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
