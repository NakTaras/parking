package com.my.parking.command.impl;

import com.my.parking.command.Command;
import com.my.parking.messagesender.MessageSender;
import com.my.parking.model.Parking;
import com.my.parking.repository.ParkingRepository;
import com.my.parking.util.LocationUtil;
import com.my.parking.util.MessageSenderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class GetNearestParkingCommand implements Command {

    @Autowired
    private ParkingRepository parkingRepository;

    @Autowired
    private MessageSender messageSender;

    @Override
    public void execute(Update update) {
        Message message = update.getMessage();
        Long currentChatID = message.getChatId();

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        Iterable<Parking> parkingList = parkingRepository.findAll();

        Parking parking = LocationUtil.getNearestParking(message.getLocation(), parkingList);

        if (parking != null) {
            keyboard.add(
                    Collections.singletonList(
                            InlineKeyboardButton.builder()
                                    .text(parking.getAddress().getName() + String.format(" - Ціна за паркомісце %.2f грн", parking.getPrice()))
                                    .callbackData("reserveDateForNearestParking_" + parking.getId())
                                    .build()));
        }

        InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder()
                .keyboard(keyboard)
                .build();

        MessageSenderUtil.sendMessage("Це найближчий до вас паркінг, натисніть на нього, щоб обрати дату бронювання",
                currentChatID,
                inlineKeyboardMarkup,
                messageSender);

    }
}
