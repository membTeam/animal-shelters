package ru.animals.utilsDEVL.entitiesenum;


/**
 * перечисление типа файла, используемого в config-command.conf
 * FILE_CONGRATULATION_ADOPTION используется для конфигурирования поздравления усыновителю животного
 * остальные параметры информационные
 */
public enum EnumTypeConfCommand {
    NONE(0), TEXT(1), FILE_CONGRATULATION_ADOPTION(10), FILE_REPORTGOOD(11),  FILE_WARNING_ADOPTION(12);

    private int index;

    EnumTypeConfCommand(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

}
