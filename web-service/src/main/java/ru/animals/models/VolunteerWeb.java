package ru.animals.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.animals.entities.enumEntity.EnumTypeAnimation;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class VolunteerWeb {
    private Long userId;
    private Long animalsIid;
}
