package ru.animals.utils.parser.enumType;

public enum EnumTypeStructConf {
    TYPE_FILE, TYPE_JSON, TYPE_PARSE, TYPE_NONE;

    public static EnumTypeStructConf of(String str) {
        return switch (str.trim().toUpperCase()) {
            case "FILE" -> TYPE_FILE;
            case "JSON" -> TYPE_JSON;
            case "PARSE" -> TYPE_PARSE;
            default -> TYPE_NONE;
        };
    }
}
