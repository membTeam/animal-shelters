package ru.animals.utilsDEVL.entitiesenum;

public enum EnumTypeMessage {
    EMPTY, START, FROM_DB, STARTREGIST, VOLONTER, BTMMENU, TEXT_MESSAGE, SIMPLE_MESSAGE, MULTY_MESSAGE;

    public static EnumTypeMessage of(String strEnum) {
        EnumTypeMessage result = switch (strEnum.trim().toUpperCase()) {
            case "SENDMESSAGE" -> EnumTypeMessage.TEXT_MESSAGE;
            case "BTMMENU" -> EnumTypeMessage.BTMMENU;
            /*case "START" -> EnumTypeMessage.START;
            case "FROM_DB", "FROMDB" -> EnumTypeMessage.FROM_DB;
            case "STARTREGIST" -> EnumTypeMessage.STARTREGIST;
            case "VOLONTER" -> EnumTypeMessage.VOLONTER;
            case "TEXT_MESSAGE", "TEXTMESSAGE" -> EnumTypeMessage.TEXT_MESSAGE;
            case "SIMPLE_MESSAGE", "SIMPLEMESSAGE" -> EnumTypeMessage.SIMPLE_MESSAGE;
            case "MULTY_MESSAGE", "MULTYMESSAGE" -> EnumTypeMessage.MULTY_MESSAGE;*/

            default -> EnumTypeMessage.EMPTY;
        };

        return result;
    }

}
