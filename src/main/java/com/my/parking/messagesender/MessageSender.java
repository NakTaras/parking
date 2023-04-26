package com.my.parking.messagesender;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface MessageSender {

    void sendMessage(SendMessage sendMessage);
}
