package ru.animals.utilsDEVL.entitiesenum;

/**
 * перечисление типа файла, используемого в config-command.conf
 * FILE_CONGRATULATION_ADOPTION используется для конфигурирования поздравления усыновителю животного
 * остальные параметры информационные
 */
public enum EnumTypeConfCommand {
    NONE(0), TEXT(1),
        FILE_EXTENSION_TERM14(2), FILE_EXTENSION_TERM30(3),
        FILE_RETURN_ANIMAL(4), FILE_MODIFICATION(5),
        FILE_CONGRATULATION_ADOPTION(10), FILE_REPORTGOOD(11);

    // FILE_EXTENSION_TERM продление срока

    private int index;

    EnumTypeConfCommand(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

}
