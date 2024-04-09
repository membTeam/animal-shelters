package ru.animals.entities;

import lombok.*;
import javax.persistence.*;
import java.util.Collection;

/**
 * Фото животного размещение в файле
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "photo_animals")
public class PhotoAnimals {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * path фото
     */
    @Column(name = "path_file", columnDefinition = "varchar(200)")
    private String pathFile;

    @Column(name = "file_size")
    private Integer fileSize;

    @Column(name = "media_type", columnDefinition = "varchar(20)")
    private String mediaType;

    @OneToOne
    @JoinColumn(name = "photo_animals_id")
    private Animals animals;

   /* @OneToMany
    @JoinColumn(name = "photo_animals_id")
    private Collection<Animals> animals;*/

//    private byte[] bytes;
}
