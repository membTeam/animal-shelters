package ru.animals.service.serviceParser;

public interface ControleService {
    boolean isExistsInMapConfig(String strCommand) throws Exception;
    boolean isExitsInMapCollback(String strCommand) throws Exception;
    String getMessageErr();
}
