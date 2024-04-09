package ru.animals.entities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.web.JsonPath;

import javax.persistence.*;
import java.util.Collection;

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

    @OneToMany
    @JoinColumn(name = "volunteer_id")
    private Collection<ContentReport> listContentReport;
}
