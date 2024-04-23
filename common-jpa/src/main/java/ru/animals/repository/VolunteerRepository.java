package ru.animals.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.animals.entities.Breeds;
import ru.animals.entities.Volunteers;

import java.util.Collection;

public interface VolunteerRepository extends JpaRepository<Volunteers, Long> {
    @Query("From volunteers where name = :str")
    Collection<Volunteers> getVolunteersByFilter(String str);

}
