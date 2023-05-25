package com.my.parking.command.impl;

import com.my.parking.command.Command;
import com.my.parking.messagesender.MessageSender;
import com.my.parking.model.Parking;
import com.my.parking.repository.ParkingPlaceRepository;
import com.my.parking.repository.ParkingRepository;
import com.my.parking.util.MessageSenderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class GetParkingListToReserveCommand implements Command {

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private ParkingRepository parkingRepository;

    @Autowired
    private ParkingPlaceRepository parkingPlaceRepository;

    @Override
    public void execute(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        Long currentChatID = callbackQuery.getFrom().getId();
        String date = callbackQuery.getData().split("_")[1];
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        List<Parking> parkingList = parkingRepository.findAvailableParkingByDate(Date.valueOf(date));

        for (Parking parking : parkingList) {
            keyboard.add(
                    Collections.singletonList(
                            InlineKeyboardButton.builder()
                                    .text(parking.getAddress().getName() + String.format(" - Ціна за паркомісце %.2f грн" +
                                                    " - Рейтинг %.1f",
                                            parking.getPrice(),
                                            parkingPlaceRepository.getRatingByParkingId(parking.getId()).orElse(0.0)))
                                    .callbackData("reserveParking_" + parking.getId() + "_" + date)
                                    .build()));
        }

        InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder()
                .keyboard(keyboard)
                .build();

        MessageSenderUtil.sendMessage("Натисніть на паркінг, щоб забронювати його на обрану дату",
                currentChatID,
                inlineKeyboardMarkup,
                messageSender);
    }
}
