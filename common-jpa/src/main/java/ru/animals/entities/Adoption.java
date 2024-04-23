package ru.animals.entities;


import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.animals.entities.enumEntity.EnumAdoptionState;

import java.util.Collection;
import java.util.Date;


/**
 * При возврате животного запись по этой таблице удаляется
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "adoption")
public class Adoption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_start", columnDefinition = "date")
    private Date dateStart;

    @Column(name = "date_finish", columnDefinition = "date")
    private Date dateFinish;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "animate_id")
    private Long animateId;

    @Column(name = "adoption_state")
    @Enumerated(EnumType.STRING)
    private EnumAdoptionState adoptionState;

    @OneToMany
    @JoinColumn(name = "adoption_id")
    @JsonIgnore
    private Collection<ContentReport> lsContentReport;
}
