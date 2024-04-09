package ru.animals.entities;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;

import lombok.*;
import org.hibernate.type.SqlTypes;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Entity
@Table(name = "content_report")
public class ContentReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "date")
    private Date date;
    private Long adoption_id;

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

    /**
     * Сведения по развемещнию фото в файле
     */
    //@Type(type = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private MetaDataPhoto metaDataPhoto;

}
