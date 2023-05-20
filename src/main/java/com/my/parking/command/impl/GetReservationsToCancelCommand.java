package com.my.parking.command.impl;

import com.my.parking.command.Command;
import com.my.parking.messagesender.MessageSender;
import com.my.parking.model.ParkingPlace;
import com.my.parking.model.User;
import com.my.parking.repository.ParkingPlaceRepository;
import com.my.parking.repository.UserRepository;
import com.my.parking.util.MessageSenderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class GetReservationsToCancelCommand implements Command {
    @Autowired
    private MessageSender messageSender;

    @Autowired
    private ParkingPlaceRepository parkingPlaceRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void execute(Update update) {
        Long currentChatID = update.getMessage().getChatId();

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        Date currentDate = Date.valueOf(LocalDate.now());
        User currentUser = userRepository.findById(currentChatID).orElse(null);

        Iterable<ParkingPlace> parkingPlaces = parkingPlaceRepository.findAllByUserAndDateGreaterThanEqual(currentUser, currentDate);

        parkingPlaces.forEach(parkingPlace -> keyboard.add(Collections.singletonList(
                InlineKeyboardButton.builder()
                        .text("№ " + parkingPlace.getId()
                                + " - " + parkingPlace.getParking().getAddress().getName()
                                + " - " + parkingPlace.getDate())
                        .callbackData("cancelReservation_" + parkingPlace.getId())
                        .build())));

        InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder()
                .keyboard(keyboard)
                .build();

        MessageSenderUtil.sendMessage("Оберіть бронювання, щоб скасувати його",
                currentChatID,
                inlineKeyboardMarkup,
                messageSender);
    }
}
