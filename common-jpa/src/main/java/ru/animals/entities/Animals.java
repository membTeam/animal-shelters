package ru.animals.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

import ru.animals.entities.commonModel.MetaDataPhoto;
import ru.animals.entities.enumEntity.EnumLimitations;

import java.util.Collection;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "animals")
@Table(uniqueConstraints = @UniqueConstraint(columnNames={"hashmetadata"}))
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class )
public class Animals {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "breed_id")
    private Long breedId;

    private String nickname;

    @Column(columnDefinition = "varchar(200)")
    @Setter
    private String hashmetadata;
    /**
     * Сведения по развемещнию фото в файле
     */
    @Type(type = "jsonb")
    @Column(name = "meta_data_photo", columnDefinition = "jsonb")
    @Setter
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

    @OneToMany
    @JoinColumn(name = "animals_id")
    @JsonIgnore
    private Collection<Adoption> lsAdoptional;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "breed_id", updatable = false, insertable = false, nullable = false)
    private Breeds breeds;

}
