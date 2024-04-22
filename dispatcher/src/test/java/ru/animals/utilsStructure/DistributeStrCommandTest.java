package ru.animals.utilsStructure;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.animals.collbackCommand.DistrCollbackCommandImpl;
import ru.animals.utils.UtilsSendMessage;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DistributeStrCommandTest {

    @Autowired
    private DistrCollbackCommandImpl commCollbackServ;

    @Autowired
    private UtilsSendMessage utilsSendMessage;

    @Test
    public void distributeStrCommand() throws Exception {
        var strucComd = utilsSendMessage.getStructCommandCollback("dbd-volunteer");

        var result = commCollbackServ.distributeStrCommand (1L, strucComd);

        assertNotNull(result);
        assertInstanceOf(SendMessage.class,result);

    }

}
