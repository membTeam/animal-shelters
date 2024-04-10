package ru.animals.entities;


import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import ru.animals.entities.enumEntity.EnumLimitations;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "animals")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
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
    @Column(name = "meta_data_photo", columnDefinition = "jsonb")
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
