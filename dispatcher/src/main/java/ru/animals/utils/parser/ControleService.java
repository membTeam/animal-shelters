package ru.animals.utils.parser;

public interface ControleService {
    boolean isExistsInMapConfig(String strCommand) throws Exception;
    boolean isExitsInMapCollback(String strCommand) throws Exception;
    String getMessageErr();
}
