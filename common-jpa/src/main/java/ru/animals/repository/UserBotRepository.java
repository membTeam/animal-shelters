package ru.animals.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.animals.models.UserBot;


public interface UserBotRepository extends JpaRepository<UserBot, Long> {

}
