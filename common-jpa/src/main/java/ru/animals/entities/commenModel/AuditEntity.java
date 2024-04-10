package ru.animals.entities.commenModel;

import javax.persistence.*;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@MappedSuperclass
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class AuditEntity {
    @LastModifiedDate
    @Column(name = "date_update")
    protected LocalDateTime dateUpdate;

    @Column(name = "dateCreate", updatable = false)
    @CreatedDate
    protected LocalDateTime dateCreate;

}
