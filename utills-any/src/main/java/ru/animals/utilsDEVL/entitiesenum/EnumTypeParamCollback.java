package ru.animals.utilsDEVL.entitiesenum;

public enum EnumTypeParamCollback {
    TCL_TXT, TCL_BTN, TCL_DBD, NONE;

    public static EnumTypeParamCollback of(String strEnum) {
        return switch (strEnum.trim().toUpperCase()) {
            case "TXT" -> EnumTypeParamCollback.TCL_TXT;
            case "BTN" -> EnumTypeParamCollback.TCL_BTN;
            case "DBD" -> EnumTypeParamCollback.TCL_DBD;
            default -> EnumTypeParamCollback.NONE;
        };
    }

}
