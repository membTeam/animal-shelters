package ru.animals.entities;


import javax.persistence.*;
import lombok.*;
import ru.animals.entities.commenModel.AuditEntity;
import ru.animals.entities.enumEntity.EnumRoleUser;

import java.util.Collection;

/**
 * fsdfs sdf
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_bot")
public class UserBot extends AuditEntity {

    // TODO: изменить идентификатор поля char_id into chat_id
    //  также сделать это в репозитории boolean isExistsUserBot(Long chartid)
    @Id
    @Column(name = "char_id")
    private Long charId;

    @Column(name = "first_name", columnDefinition = "varchar(50)")
    private String firstName;

    @Column(name = "last_name", columnDefinition = "varchar(50)")
    private String lastName;

    @Column(columnDefinition = "varchar(50)")
    @Enumerated(EnumType.STRING)
    private EnumRoleUser role;

    @Column(columnDefinition = "varchar(20)")
    private String phone;

    @Column(columnDefinition = "varchar(150)")
    private String email;

    @OneToMany
    @JoinColumn(name = "user_id")
    Collection<Adoption> listUserBot;

    @OneToMany
    @JoinColumn(name = "user_id")
    private Collection<ContentReport> listContentReport;

}
