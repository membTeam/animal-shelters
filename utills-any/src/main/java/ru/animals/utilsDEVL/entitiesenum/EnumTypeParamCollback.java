package ru.animals.utilsDEVL.entitiesenum;

public enum EnumTypeParamCollback {
    TCL_TXT, TCL_BTN, TCL_DBD, TCL_DST, NONE;

    public static EnumTypeParamCollback of(String strEnum) {
        return switch (strEnum.trim().toUpperCase()) {
            case "TXT" -> TCL_TXT;
            case "BTN" -> TCL_BTN;
            case "DBD" -> TCL_DBD;
            case "DST" -> TCL_DST;
            default -> NONE;
        };
    }

    public static EnumTypeParamCollback enumByString(String str) {
        var sub = str.trim().substring(0,3);
        return of(sub);
    }

}
