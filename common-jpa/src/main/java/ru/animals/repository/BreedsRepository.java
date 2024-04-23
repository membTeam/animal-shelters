package ru.animals.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.animals.entities.Breeds;

import java.util.Collection;

public interface BreedsRepository extends JpaRepository<Breeds, Long> {
    @Query("From breeds where typeAnimationsId = :typeid")
    Collection<Breeds> getBreedsFromFilter(Long typeid);

    Collection<Breeds> findAllByTypeAnimationsId(Long typeId);
}
