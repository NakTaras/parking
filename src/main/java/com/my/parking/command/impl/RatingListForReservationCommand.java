package com.my.parking.command.impl;

import com.my.parking.command.Command;
import com.my.parking.messagesender.MessageSender;
import com.my.parking.util.MessageSenderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

@Component
public class RatingListForReservationCommand implements Command {
    @Autowired
    private MessageSender messageSender;

    @Override
    public void execute(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        Long currentChatID = callbackQuery.getFrom().getId();
        String[] callbackQueryData = callbackQuery.getData().split("_");
        long parkingPlaceId = Long.parseLong(callbackQueryData[1]);
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        IntStream.range(1, 6).forEach(value -> keyboard.add(Collections.singletonList(
                InlineKeyboardButton.builder()
                        .text(String.valueOf(value))
                        .callbackData("rateReservation_" + parkingPlaceId + "_" + value)
                        .build())));

        InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder()
                .keyboard(keyboard)
                .build();

        MessageSenderUtil.sendMessage("Оцініть бронювання №" + parkingPlaceId + " , обравши необхідне значення",
                currentChatID,
                inlineKeyboardMarkup,
                messageSender);
    }
}
