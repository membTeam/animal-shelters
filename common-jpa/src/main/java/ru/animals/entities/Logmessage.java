package ru.animals.entities;

import lombok.*;
import ru.animals.utilsDEVL.entitiesenum.EnumTypeConfCommand;

import javax.persistence.Entity;
import java.time.LocalDateTime;
import javax.persistence.*;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "logmessage")
public class Logmessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Long chatId;
    private LocalDateTime dateMessage;
    private String message;

    /**
     * Группировака между текстовым сообщениями и специальными,
     * которые конфигурируются на основе command-config.conf
     */
    @Enumerated(EnumType.STRING)
    private EnumTypeConfCommand typeConfCommand;
}
