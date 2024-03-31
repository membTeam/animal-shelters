package ru.animals.entities;


import lombok.*;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "shelters")
public class Shelters {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name_shelterl", columnDefinition = "varchar(100)")
    private String nameShelterl;

    @Column(columnDefinition = "varchar(200)")
    private String adress;

    @Column(columnDefinition = "varchar(20)")
    private String contacts;

    @Column(name = "work_schedule", columnDefinition = "varchar(200)")
    private String workSchedule;   // часы работы

    @Column(name = "rules_conduct", columnDefinition = "varchar(500)")
    private String rulesConduct;    // правилаПоведения

    @Column(name = "driving_directions", columnDefinition = "varchar(200)")
    private String drivingDirections;  // схемаПроезда

    @Column(name = "story_Shelter", columnDefinition = "text")
    private String storyShelter;

    @OneToOne
    private Breeds breeds;

}
