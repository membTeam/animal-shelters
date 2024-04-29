package ru.animals.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.animals.entities.Logmessage;

import java.util.List;

public interface LogmessageRepository extends JpaRepository <Logmessage, Long> {
    List<Logmessage> findAllByChatId(Long chatid);
}
