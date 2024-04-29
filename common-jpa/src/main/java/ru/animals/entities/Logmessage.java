package ru.animals.entities;

import lombok.*;
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
}
