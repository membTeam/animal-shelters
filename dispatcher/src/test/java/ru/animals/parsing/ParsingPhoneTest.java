package ru.animals.parsing;

import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParsingPhoneTest {

    @Test
    public void parsingNumberPhose() {
        var text = "Вася (+7)(918) (570) (20-33)";

        var index = text.indexOf("(");
        var userName = text.substring(0, index).trim();
        var strPhone = text.substring(index);

        var strPattern = "^(\\(\\+7\\)) (\\(9\\d{2}\\)) (\\(\\d{3}\\)) (\\(\\d{2}-\\d{2}\\))$";
        var pattern = Pattern.compile(strPattern);

        Matcher matcher = pattern.matcher(strPhone);

        if (matcher.find()) {
            System.out.println("find");
            System.out.println(matcher.groupCount());
            var strFormat = String.format("%s %s %s %s",
                    matcher.group(1), matcher.group(2),
                    matcher.group(3), matcher.group(4) );

            System.out.println(strFormat);
        } else {
            System.out.println("no find");
        }
    }

}
