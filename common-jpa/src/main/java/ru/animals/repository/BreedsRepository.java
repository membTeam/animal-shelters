package ru.animals.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.animals.entities.Breeds;

import java.util.Collection;

public interface BreedsRepository extends JpaRepository<Breeds, Long> {

    Collection<Breeds> findAllByTypeAnimationsId(Long typeId);
}
