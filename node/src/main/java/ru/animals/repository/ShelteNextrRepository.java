package ru.animals.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.animals.entities.Shelters;

import java.util.Collection;

public interface ShelteNextrRepository extends JpaRepository<Shelters, Long> {
    @Query("From shelters sh where sh.rulesConduct = :rulesConduct")
    Collection<Shelters> getDataShelters(String rulesConduct);

    //Collection<Shelters> findAllByRulesConduct(String str);

/*    @Query("select sh FROM Shelters sh where sh.rulesConduct = :str")
    List<Shelters> getAnyDataShelters(String str);*/

}
