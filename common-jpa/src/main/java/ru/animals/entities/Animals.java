package ru.animals.entities;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.animals.entities.enumEntity.EnumLimitations;


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

    @Column(name = "breed_id")
    private Long breedId;

    private String nickname;

    /**
     * Сведения по развемещнию фото в файле
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private MetaDataPhoto metaDataPhoto;

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
