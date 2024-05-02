package ru.animals.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.animals.entities.Animals;
import ru.animals.entities.commonModel.MetaDataPhoto;

public interface AnimalsRepository extends JpaRepository<Animals, Long> {
    Animals findByHashmetadata(String info);

    @Query("select an.metaDataPhoto from animals an where an.hashmetadata = :metaData")
    MetaDataPhoto findByHashmetadataNext(String metaData);

    @Query("select an from animals an where an.hashmetadata = :metaData")
    Animals findAnimalsByHashmetadataNext(String metaData);

}
