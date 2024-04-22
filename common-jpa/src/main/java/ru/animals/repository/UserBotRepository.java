package ru.animals.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.animals.entities.UserBot;

import java.util.Collection;
import java.util.Optional;


public interface UserBotRepository extends JpaRepository<UserBot, Long> {
    @Query("From UserBot where userChat > 0")
    Collection<UserBot> lsUserBot();

    Optional<UserBot> findByChatId(Long charId);
}
