package ru.animals.entities;


import lombok.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Getter
@Setter
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


    @OneToMany
    @JoinColumn(name = "content_report_id")
    private Collection<contentReport> listContentReport;

}
