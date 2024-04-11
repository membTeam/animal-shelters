package ru.animals.repository;

import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.animals.entities.ContentReport;
import ru.animals.entities.Shelters;
import ru.animals.entities.Volunteers;

import java.util.Collection;
import java.util.Optional;

public interface CommonReposities extends JpaRepository<ContentReport, Long> {
    @Query("from shelters")
    Collection<Shelters> findAllShelters();

    @Query("from volunteers")
    Collection<Volunteers> findAllVolunteer();

    // TODO: изменить идентификатор поля char_id into chat_id
    @Query(value = "select exists(select * from user_bot where char_id = :chatid) res", nativeQuery = true)
    boolean isExistsUserBot(Long chatid);

}
