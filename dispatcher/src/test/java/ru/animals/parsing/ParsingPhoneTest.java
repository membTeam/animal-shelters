package ru.animals.parsing;

import org.junit.jupiter.api.Test;
import ru.animals.utilsDEVL.entitiesenum.ParsingStrPhoto;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class ParsingPhoneTest {

    @Test
    public void parsingStrPhoto_of() {
        var str = "pht-photorep-rep-dog-48906";

        var obj = ParsingStrPhoto.of(str);

        assertEquals("pht", obj.getKey());
        assertEquals("photorep", obj.getDistination());
        assertEquals("rep-dog-48906", obj.getResource());

    }

    @Test
    public void parsingNumberPhose() {
        var text = "Вася (+7) (918) (570) (20-33)";

        var index = text.indexOf("(");
        var userName = text.substring(0, index).trim();
        var strPhone = text.substring(index);

        var strPattern = "^(\\(\\+7\\)) (\\(9\\d{2}\\)) (\\(\\d{3}\\)) (\\(\\d{2}-\\d{2}\\))$";
        var pattern = Pattern.compile(strPattern);

        Matcher matcher = pattern.matcher(strPhone);
        assertDoesNotThrow(()-> matcher.find() );

        var strFormat = String.format("%s %s %s %s",
                matcher.group(1), matcher.group(2),
                matcher.group(3), matcher.group(4) );

        assertEquals("(+7) (918) (570) (20-33)", strFormat);
    }

}
