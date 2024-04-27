package ru.animals.entities;

import javax.persistence.*;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import lombok.*;
import ru.animals.entities.commonModel.MetaDataPhoto;
import ru.animals.entities.enumEntity.EnumStatusReport;

import java.time.LocalDate;
import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "content_report", uniqueConstraints = @UniqueConstraint(columnNames={"hashmetadata"}))
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class ContentReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "date")
    private LocalDate date;

    @Column(name = "adoption_id")
    private Long adoptionId;

    /**
     * Питание животного
     */
    @Column(name = "animal_diet", columnDefinition = "varchar(150)")
    private String animalDiet;

    /**
     * Общее самочувствие
     */
    @Column(name = "general_well_being", columnDefinition = "varchar(150)")
    private String generalWellBeing;

    /**
     * Изменение привычек
     */
    @Column(name = "change_behavior", columnDefinition = "varchar(150)")
    private String changeBehavior;

    @Column(name = "status_report")
    @Enumerated(EnumType.STRING)
    private EnumStatusReport statusReport;

    @Setter
    @Column(columnDefinition = "varchar(200)")
    private String hashmetadata;

    /**
     * Сведения по развемещнию фото в файле
     */
    @Type(type = "jsonb")
    @Setter
    @Column(name = "meta_data_photo", columnDefinition = "jsonb")
    private MetaDataPhoto metaDataPhoto;

}
