package ru.animals.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.animals.entities.Adoption;
import ru.animals.entities.enumEntity.EnumAdoptionState;

import java.util.Collection;
import java.util.Optional;

public interface AdoptionalRepository extends JpaRepository<Adoption, Long> {

    @Query("FROM adoption a where a.userId = :id and adoptionState = :state")
    Collection<Adoption> findByUserIdAndOnProbational(Long id, EnumAdoptionState state);

    @Query(value = "select exists (select * from adoption a where a.user_id = :userid and a.animals_id = :animalsid) res;", nativeQuery = true)
    Boolean findByUserIdAndAnimalsId(Long userid, Long animalsid);

}
