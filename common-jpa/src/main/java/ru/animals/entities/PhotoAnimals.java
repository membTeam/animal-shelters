package ru.animals.entities;

import lombok.*;
import javax.persistence.*;
import java.util.Collection;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "photo_animals")
public class PhotoAnimals {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_size")
    private Integer fileSize;

    @Column(name = "media_type", columnDefinition = "varchar(20)")
    private String mediaType;

    @Column(name = "file_path", columnDefinition = "varchar(150)")
    private String filePath;

    @OneToMany
    @JoinColumn(name = "photo_animals_id")
    private Collection<Animals> animals;

    private byte[] bytes;
}
