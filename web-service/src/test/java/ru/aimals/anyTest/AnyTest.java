package ru.aimals.anyTest;

import org.junit.jupiter.api.Test;
import ru.animals.entities.enumEntity.EnumTypeAnimation;

import static org.junit.jupiter.api.Assertions.*;

public class AnyTest {

    @Test
    public void EnumTypeAnimation_dog() {
        var enumData = EnumTypeAnimation.DOG;

        assertEquals(1, enumData.getTypeAnimation());
    }

    @Test
    public void EnumTypeAnimation_cat() {
        var enumData = EnumTypeAnimation.CAT;

        assertEquals(2, enumData.getTypeAnimation());
    }

}
