package ru.animals.entities;


import javax.persistence.*;
import lombok.*;

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

    private Long user_id;
    private Long animate_id;

}
