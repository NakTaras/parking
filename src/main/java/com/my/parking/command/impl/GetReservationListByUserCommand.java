package com.my.parking.command.impl;

import com.my.parking.command.Command;
import com.my.parking.messagesender.MessageSender;
import com.my.parking.model.ParkingPlace;
import com.my.parking.model.User;
import com.my.parking.repository.ParkingPlaceRepository;
import com.my.parking.repository.UserRepository;
import com.my.parking.util.MessageSenderUtil;
import com.my.parking.util.ReplyKeyboardMarkupUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.sql.Date;
import java.time.LocalDate;
import java.util.stream.StreamSupport;

@Component
public class GetReservationListByUserCommand implements Command {

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private ParkingPlaceRepository parkingPlaceRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void execute(Update update) {
        Long currentChatID = update.getMessage().getChatId();
        User currentUser = userRepository.findById(currentChatID).orElse(null);
        ReplyKeyboardMarkup replyKeyboardMarkup = ReplyKeyboardMarkupUtil.createReplyKeyboardMarkup(currentUser);
        Date currentDate = Date.valueOf(LocalDate.now());

        Iterable<ParkingPlace> parkingPlaces = parkingPlaceRepository.findAllByUserAndDateGreaterThanEqual(currentUser, currentDate);
        StringBuilder stringBuilder;

        if (StreamSupport.stream(parkingPlaces.spliterator(), false).findAny().isEmpty()) {
            stringBuilder = new StringBuilder("У вас немає активних бронювань");
        } else {
            stringBuilder = new StringBuilder("Ви маєте такі активні бронювання:\n\n");

            parkingPlaces.forEach(parkingPlace -> stringBuilder.append("Номер резерву: ").append(parkingPlace.getId()).append(System.lineSeparator())
                    .append("Адреса паркінгу: ").append(parkingPlace.getParking().getAddress().getName()).append(System.lineSeparator())
                    .append("Дата бронювання: ").append(parkingPlace.getDate()).append(System.lineSeparator())
                    .append(String.format("Ціна за паркомісце: %.2f грн", parkingPlace.getParking().getPrice())).append(System.lineSeparator()).append(System.lineSeparator()));
        }

        MessageSenderUtil.sendMessage(stringBuilder.toString(),
                currentChatID,
                replyKeyboardMarkup,
                messageSender);
    }
}
