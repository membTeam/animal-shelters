package ru.animals.entities;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import java.util.Collection;

/**
 * Сущность породы животных
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "breeds")
public class Breeds {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "varchar(100)")
    private String breed;

    @Column(name = "type_animations_id")
    private Long typeAnimationsId;

    @OneToMany
    @JoinColumn(name = "breed_id")
    @JsonIgnore
    private Collection<Animals> animals;
}
