package ru.animals.utils;


import ru.animals.utils.parser.StructForBaseConfig;

/**
 * используется в UtilSendMessage
 */
public interface UtilsSendMessageServ {

    boolean isExistsInMapConfig(String strCommand) throws Exception;
    boolean isExitsInMapCollback(String strCommand) throws Exception;
    String getMessageErr();

    StructForBaseConfig getStructureForCongratulation();
}
