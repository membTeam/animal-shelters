package ru.animals.models;


import lombok.*;
import javax.persistence.*;

import ru.animals.models.commenModel.AuditEntity;
import ru.animals.models.enumEntity.EnumRoleUser;
import ru.animals.models.enumEntity.EnumState;


import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class UserBot extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "varchar(50)")
    @Enumerated(EnumType.STRING)
    private EnumRoleUser role;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(50)")
    private EnumState state;

}
