package ru.animals.utilsdevl;

public final class VOresult<T> {
    public final boolean RESULT;
    public final String MESSAGE;
    public final T VALUE;

    public VOresult(String mes ) {
        RESULT = false;
        MESSAGE = mes;
        VALUE = null;
    }

    public VOresult(T value) {
        RESULT = true;
        MESSAGE = "ok";
        VALUE = value;
    }

}
