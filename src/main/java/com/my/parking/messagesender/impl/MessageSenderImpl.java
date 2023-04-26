package com.my.parking.messagesender.impl;

import com.my.parking.messagesender.MessageSender;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class MessageSenderImpl implements MessageSender {

    private TelegramLongPollingBot telegramBot;

    public MessageSenderImpl(@Lazy TelegramLongPollingBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @Override
    public void sendMessage(SendMessage sendMessage) {
        try {
            telegramBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
