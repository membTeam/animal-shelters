package ru.animals.utilsDEVL.entitiesenum;

public enum EnumTypeParamCollback {
    TEXT_MESSAGE, BTN_MENU, DBD_DATABASE, NONE;

    public static EnumTypeParamCollback of(String strEnum) {
        return switch (strEnum.trim().toUpperCase()) {
            case "TXT" -> EnumTypeParamCollback.TEXT_MESSAGE;
            case "BTN" -> EnumTypeParamCollback.BTN_MENU;
            case "DBD" -> EnumTypeParamCollback.BTN_MENU;
            default -> EnumTypeParamCollback.NONE;
        };
    }

}
