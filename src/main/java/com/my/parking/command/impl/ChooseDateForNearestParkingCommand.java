package com.my.parking.command.impl;

import com.my.parking.command.Command;
import com.my.parking.messagesender.MessageSender;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class ChooseDateForNearestParkingCommand implements Command {

    @Autowired
    private ParkingPlaceRepository parkingPlaceRepository;

    @Autowired
    private ParkingRepository parkingRepository;

    @Autowired
    private MessageSender messageSender;

    @Override
    public void execute(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        long currentChatID = callbackQuery.getFrom().getId();

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        LocalDate date = LocalDate.now();
        long parkingId = Long.parseLong(callbackQuery.getData().split("_")[1]);
        for (int i = 1; i <= 7; i++) {
            Integer amount = parkingPlaceRepository.getAmountOfAvailableParkingPlaces(parkingId, Date.valueOf(date));
            if (amount == null) {
                amount = parkingRepository.findNumberOfParkingPlaces(parkingId);
            }
            keyboard.add(
                    Collections.singletonList(
                            InlineKeyboardButton.builder()
                                    .text(date
                                            + " - Вільних місць "
                                            + amount)
                                    .callbackData("reserveParking_" + parkingId + "_" + date)
                                    .build()));
            date = date.plusDays(1);
        }

        keyboard.add(
                Collections.singletonList(
                        InlineKeyboardButton.builder()
                                .text("Далі")
                                .callbackData("nextDateForNearestParking_" + parkingId + "_" + date)
                                .build()));

        InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder()
                .keyboard(keyboard)
                .build();

        MessageSenderUtil.sendMessage("Оберіть потрібну дату",
                currentChatID,
                inlineKeyboardMarkup,
                messageSender);
    }
}
