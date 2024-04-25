package ru.animals.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.animals.entities.Adoption;
import ru.animals.entities.enumEntity.EnumAdoptionState;

import java.util.Collection;
import java.util.List;

public interface AdoptionalRepository extends JpaRepository<Adoption, Long> {

    @Query("FROM adoption a where a.userId = :id and adoptionState = :state")
    Collection<Adoption> findByUserIdAndOnProbational(Long id, EnumAdoptionState state);

    @Query(value = "FROM adoption a where a.userId = :id and a.adoptionState = 'ON_PROBATION' ")
    List<Adoption> findByUserIdForONPROBATION(Long id);

    @Query(value = "select exists (select * from adoption a where a.user_id = :userid and a.animals_id = :animalsid) res;", nativeQuery = true)
    Boolean findByUserIdAndAnimalsId(Long userid, Long animalsid);

    @Query(value = "select exists(select * from adoption a where user_id = :userId) res",nativeQuery = true)
    boolean containsByUserId(Long userId);

}
