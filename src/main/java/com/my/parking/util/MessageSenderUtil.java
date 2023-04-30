package com.my.parking.util;

import com.my.parking.messagesender.MessageSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

public class MessageSenderUtil {

    private MessageSenderUtil() {
    }

    public static void sendMessage(String messageText, long chatId, ReplyKeyboard replyKeyboard, MessageSender messageSender) {
        messageSender.sendMessage(
                SendMessage.builder()
                        .text(messageText)
                        .parseMode("HTML")
                        .replyMarkup(replyKeyboard)
                        .chatId(chatId)
                        .build());
    }
}
