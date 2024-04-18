package ru.animals.utilsDEVL.entitiesenum;

/**
 * Перечисление по типам сообщений
 */
public enum EnumTypeParamMessage {
    EMPTY, SELMENU, BTMMENU, TEXT_MESSAGE;

    public static EnumTypeParamMessage of(String argStrEnum) {

        var enumStr = argStrEnum.trim().toUpperCase();

        if (enumStr.charAt(0) == '/') {
            enumStr = argStrEnum.substring(1);
        }

        return switch (enumStr) {
            case "SENDMESSAGE" -> EnumTypeParamMessage.TEXT_MESSAGE;
            case "BTMMENU" -> EnumTypeParamMessage.BTMMENU;
            case "SELMENU" -> EnumTypeParamMessage.SELMENU;

            default -> EnumTypeParamMessage.EMPTY;
        };

    }

}
