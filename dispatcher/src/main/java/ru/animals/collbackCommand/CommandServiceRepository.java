package ru.animals.collbackCommand;

import ru.animals.repository.VolunteerRepository;

public interface CommandServiceRepository {
    VolunteerRepository getVolunteerRepository();

}
