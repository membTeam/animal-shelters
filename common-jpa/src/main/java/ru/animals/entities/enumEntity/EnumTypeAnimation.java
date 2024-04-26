package ru.animals.entities.enumEntity;

public enum EnumTypeAnimation {
    DOG(1), CAT(2);

    private int typeAnimation;

    EnumTypeAnimation(int index) {
        typeAnimation = index;
    }

    public int getTypeAnimation() {
        return typeAnimation;
    }

    public static String getStringTypeAnimation(int number) {
        return switch (number) {
            case 1 -> "DOG";
            case 2 -> "CAT";
            default -> "none";
        };
    }

}
