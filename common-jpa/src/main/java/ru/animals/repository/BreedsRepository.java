package ru.animals.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.animals.entities.Breeds;

import java.util.Collection;
import java.util.List;

public interface BreedsRepository extends JpaRepository<Breeds, Long> {
    @Query("From breeds where typeAnimationsId = :typeid")
    Collection<Breeds> getBreedsFromFilter(Long typeid);

    List<Breeds> findAllByTypeAnimationsId(Long typeId);

    @Query(value = "select b.type_animations_id from adoption ad" +
            " left join animals an on ad.animals_id = an.id" +
            " and ad.user_id = :userid" +
            " and ad.adoption_state = 'ON_PROBATION'" +
            " left join breeds b on an.breed_id = b.id ;", nativeQuery = true)
    Integer getTypeAnimationFromReport(Integer userid);

    @Query(value = "select q.breed, q.nikname, q.url from fun_list_animals() q ", nativeQuery = true)
    List<List<String>> getDataForAnimation();

}
