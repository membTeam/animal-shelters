package ru.animals.verifyConfFiles;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.animals.collbackCommand.CommonCollbackService;
import ru.animals.utils.UtilsSendMessage;
import ru.animals.utilsDEVL.FileAPI;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CollbackCommandTest {

    @Autowired
    private UtilsSendMessage utilsSendMessage;

    @Autowired
    private CommonCollbackService commonCollbackService;

    @Test
    public void verifyConfigCollback() throws Exception {

        var resLoad = FileAPI.readConfiguration("config-collback.conf");
        List<String> lsStr = resLoad.getValue();

        var lsClass = new HashSet<String>();
        lsStr.stream()
                .filter(str-> str.substring(0,3).toLowerCase().equals("dbd"))
                .forEach(str-> {
                    var value = str.split("##")[1].trim().toLowerCase();
                    lsClass.add(value);
                });

        List<String> lsError = new ArrayList<>();
        lsClass.stream().forEach(str-> {
            try {
                var structCommand = utilsSendMessage.getStructCommandCollback(str);
                var res = commonCollbackService.distributeStrCommand(1L, structCommand);
                if (res == null) {
                    lsError.add(str);
                }
            } catch (Exception e) {
                lsError.add(e.getMessage());
            }
        });

        if (lsStr.size() > 0) {
            System.out.println(lsError);
        }

        assertNotNull(lsError.size() == 0);

    }

}
