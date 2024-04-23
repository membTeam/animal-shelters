package ru.animals.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.animals.entities.Shelters;

import java.util.Collection;
import java.util.List;

public interface ShelterRepository extends JpaRepository<Shelters, Long> {
    @Query("From shelters sh where sh.rulesConduct = :rulesConduct")
    List<Shelters> getDataShelters(String rulesConduct);

}
