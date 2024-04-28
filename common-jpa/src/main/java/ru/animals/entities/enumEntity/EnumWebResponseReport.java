package ru.animals.entities.enumEntity;

public enum EnumWebResponseReport {
    Продлить_на_14_дней(1),
    Продлить_на_30_дней(2),
    Возврат_животного(3),
    Отчет_не_принят(4),
    Отчет_принят(5);

    public static EnumStatusReport convertToStatusReport(int index) {
        return switch (index) {
            case 1 -> EnumStatusReport.EXTENSION_TERM_14;
            case 2 -> EnumStatusReport.EXTENSION_TERM_30;
            case 3 -> EnumStatusReport.RETURN_ANIMAL;
            case 4 -> EnumStatusReport.MODIFICATION;
            case 5 -> EnumStatusReport.REPORT_ACCEPTED;
            default -> EnumStatusReport.NONE;
        };

        // ADOPTED, если истек испытательный срок и последний отчет принят
    }

    private int index;

    EnumWebResponseReport(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

}
