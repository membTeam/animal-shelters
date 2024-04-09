package ru.animals.entities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Сущность волнтеры
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "volunteers")
public class Volunteers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "varchar(100)")
    private String name;

    @Column(columnDefinition = "varchar(50)")
    private String phone;

    @Column(name = "chart_name", columnDefinition = "varchar(50)")
    private String chartName;

}
