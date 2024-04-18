package ru.animals.utils;


/**
 * используется в UtilSendMessage
 */
public interface UtilsSendMessageServ {
    boolean isExistsInMapConfig(String strCommand) throws Exception;
    boolean isExitsInMapCollback(String strCommand) throws Exception;
    String getMessageErr();
}
