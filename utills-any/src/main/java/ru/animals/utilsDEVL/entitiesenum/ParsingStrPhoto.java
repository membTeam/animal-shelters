package ru.animals.utilsDEVL.entitiesenum;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ParsingStrPhoto {
    private final String key;
    private final String distination;
    private final String resource;

    public static ParsingStrPhoto of(String strforParsing) {

        String[] arrStr = strforParsing.split("-", 3);

        return ParsingStrPhoto.builder()
                .key(arrStr[0])
                .distination(arrStr[1])
                .resource(arrStr[2])
                .build();
    }

}
