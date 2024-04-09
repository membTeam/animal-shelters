package ru.animals.entities;


import lombok.*;

import javax.persistence.*;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "shelters")
public class Shelters {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name_shelter", columnDefinition = "varchar(100)")
    private String nameShelter;

    @Column(columnDefinition = "varchar(200)")
    private String adress;

    /**
     * Контактные данные ссылка на текстовый файл
     */
    @Column(columnDefinition = "varchar(100)")
    private String contacts;

    /**
     * Часы работы Ссылка на текстовый файл
     */
    @Column(name = "work_schedule", columnDefinition = "varchar(100)")
    private String workSchedule;

    /**
     * Правила поведения Ссылка на текстовый файл
     */
    @Column(name = "rules_conduct", columnDefinition = "varchar(100)")
    private String rulesConduct;

    /**
     * Схема проезда Ссылка на текстовый файл
     */
    @Column(name = "driving_directions", columnDefinition = "varchar(100)")
    private String drivingDirections;  // схемаПроезда

    /**
     * История о приюте ссылка на текстовый файл
     */
    @Column(name = "history_shelter", columnDefinition = "varchar(100)")
    private String historyShelter;

    @OneToOne
    private TypeAnimations typeAnimations;

}
