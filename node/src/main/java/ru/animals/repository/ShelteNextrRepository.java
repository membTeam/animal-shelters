package ru.animals.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.animals.entities.Shelters;

public interface ShelteNextrRepository extends JpaRepository<Shelters, Long> {
/*    @Query("From Shelters sh where sh.rulesConduct = :rulesConduct")
    List<Shelters> getDataShelters(String rulesConduct);*/

    //Collection<Shelters> findAllByRulesConduct(String str);

/*    @Query("select sh FROM Shelters sh where sh.rulesConduct = :str")
    List<Shelters> getAnyDataShelters(String str);*/

}
