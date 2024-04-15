package ru.animals.utilsDEVL.entitiesenum;

public enum EnumTypeParamMessage {
    EMPTY, START, BTMMENU, TEXT_MESSAGE;

    public static EnumTypeParamMessage of(String argStrEnum) {

        var enumStr = argStrEnum.trim().toUpperCase();

        if (enumStr.charAt(0) == '/') {
            enumStr = argStrEnum.substring(1);
        }

        return switch (enumStr) {
            case "SENDMESSAGE" -> EnumTypeParamMessage.TEXT_MESSAGE;
            case "BTMMENU" -> EnumTypeParamMessage.BTMMENU;
            case "START" -> EnumTypeParamMessage.START;

            default -> EnumTypeParamMessage.EMPTY;
        };

    }

}
