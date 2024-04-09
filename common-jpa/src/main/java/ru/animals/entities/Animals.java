package ru.animals.entities;


import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import ru.animals.entities.enumEntity.EnumLimitations;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
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
    @Type(type = "jsonb")
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
