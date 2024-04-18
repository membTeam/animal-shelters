package ru.animals.collbackCommand.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class CollbackComdDefaultSendMess {
    public static SendMessage defaultSendMessage(long chatId, String text) {
        var sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);

        return sendMessage;
    }
}
