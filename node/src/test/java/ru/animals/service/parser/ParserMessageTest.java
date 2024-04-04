package ru.animals.service.parser;


import ru.animals.utils.ParsingMessage;
import org.junit.jupiter.api.Test;

public class ParserMessageTest {

    @Test
    public void parsingMessage() {
        var strTempl = "animalcontent -t file -v sendmessage -d animal-content.txt -h что он содержит";
        var parsing = ParsingMessage.parsingMessage(strTempl);

        if (parsing.RESULT) {
            System.out.println(parsing.VALUE);
        }

        /*var strMessage = "animalcontent -t file -v sendmessage -d animal-content.txt -h что он содержит";
        var strPattern = "(^\\w+)\\s+-t\\s+(\\w+)\\s+-v\\s+([a-zA-Z]+)"+
                "\\s+-d\\s+([a-zA-Z]+-[a-zA-Z]+.txt)|(\\w+.txt)" +
                "\\s+-h\\s+([а-яА-ЯёЁa-zA-Z0-9]+$)";

        var pattern = Pattern.compile(strPattern);
        var matcher = pattern.matcher(strMessage);*/


        //var strMessage = " animalcontent.txt ";
        //var strPattern = "\\s+((\\w+-\\w+.txt)|(\\w+.txt))";

        /*var strMessage = "animalcontent -t file  -v sendmessage -d animal-content.txt -d animal-content.txt  -h this is что он ok содержит";
        var strPattern = "\\s+-h\\s(.+)";
        var pattern = Pattern.compile(strPattern);
        var matcher = pattern.matcher(strMessage);

        if (matcher.find()) {
            System.out.println(matcher.group(1));
            ;
        } else {
            System.out.println("Ошибка");
        }*/

    }

}
