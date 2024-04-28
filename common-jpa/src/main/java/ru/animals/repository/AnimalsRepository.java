package ru.animals.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.animals.entities.Animals;

public interface AnimalsRepository extends JpaRepository<Animals, Long> {
    Animals findByHashmetadata(String info);
}
