package ru.animals.utilsStructure;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.animals.utils.UtilsSendMessage;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class UtilsSendMessageTest {

    @Autowired
    private UtilsSendMessage utilsSendMessage;

    @Test
    public void verifyLoadData() {

        var res = utilsSendMessage.isERROR();

        if (res) {
            System.out.println(utilsSendMessage.getMessageErr());
        }

        assertFalse(res);
    }

}
