package com.my.parking;

import com.my.parking.processor.impl.CallBackQueryProcessor;
import com.my.parking.processor.impl.MessageProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class ParkingBot extends TelegramLongPollingBot {

    private final MessageProcessor messageProcessor;
    private final CallBackQueryProcessor callBackQueryProcessor;

    @Value("${telegram.bot.username}")
    private String userName;
    @Value("${telegram.bot.token}")
    private String token;

    public ParkingBot(MessageProcessor messageProcessor, CallBackQueryProcessor callBackQueryProcessor) {
        this.messageProcessor = messageProcessor;
        this.callBackQueryProcessor = callBackQueryProcessor;
    }

    @Override
    public String getBotUsername() {
        return userName;
    }

    @Override
    public String getBotToken() {
        return token;
    }
    
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            messageProcessor.process(update);
        } else if (update.hasCallbackQuery()) {
            callBackQueryProcessor.process(update);
        }
    }
}
