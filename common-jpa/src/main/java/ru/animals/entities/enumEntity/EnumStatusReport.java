package ru.animals.entities.enumEntity;

public enum EnumStatusReport {
    RECEIPT_REPORT(50),
    MODIFICATION(1),
    EXTENSION_TERM_14(2),
    EXTENSION_TERM_30(3),
    RETURN_ANIMAL(10),
    REPORT_ACCEPTED(100),
    ADOPTED(101),
    NONE(99);

    private int index;

    EnumStatusReport(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

}
