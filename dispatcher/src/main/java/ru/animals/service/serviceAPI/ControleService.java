package ru.animals.service.serviceAPI;

public interface ControleService {
    boolean isExistsInMapConfig(String strCommand) throws Exception;
    String getMessageErr();
}
