package ru.animals.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.animals.entities.UserBot;


public interface UserBotRepository extends JpaRepository<UserBot, Long> {

}
