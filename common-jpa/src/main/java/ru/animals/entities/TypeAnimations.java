package ru.animals.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "type_animations")
public class TypeAnimations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type_animation", columnDefinition = "varchar(30)")
    private String typeAnimation;

    @OneToMany
    @JoinColumn(name = "type_animations_id")
    @JsonIgnore
    private Collection<Breeds> breeds;

}
