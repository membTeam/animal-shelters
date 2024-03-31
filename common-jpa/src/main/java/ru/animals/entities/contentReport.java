package ru.animals.entities;


import lombok.*;

import javax.persistence.*;
import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "content_report")
public class contentReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "date")
    private Date date;
    private Long adoption_id;

    @Column(name = "animal_diet", columnDefinition = "varchar(150)")
    private String animalDiet;

    @Column(name = "general_well_being", columnDefinition = "varchar(150)")
    private String generalWellBeing;

    @Column(name = "change_behavior", columnDefinition = "varchar(150)")
    private String changeBehavior;

    @Column(name = "content_report_id")
    private Long contentReportId;

    private byte[] bytes;

}
