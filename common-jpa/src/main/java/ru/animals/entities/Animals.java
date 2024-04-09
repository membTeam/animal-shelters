package ru.animals.entities;

import lombok.*;
import ru.animals.entities.enumEntity.EnumLimitations;

import javax.persistence.*;
import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "animals")
public class Animals {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "photo_animals_id")
    private Long photoAnimalsId;

    @Column(name = "breed_id")
    private Long breedId;

    private String nickname;

    /**
     * true животное можно усыновить false
     */
    private boolean status;

    /**
     * Использование строкового идентификатора
     * Состояние животного
     */
    @Enumerated(EnumType.STRING)
    private EnumLimitations limitations;
}
