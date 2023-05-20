package com.my.parking.command.impl;

import com.my.parking.command.Command;
import com.my.parking.messagesender.MessageSender;
import com.my.parking.model.Parking;
import com.my.parking.model.ParkingPlace;
import com.my.parking.model.User;
import com.my.parking.repository.ParkingPlaceRepository;
import com.my.parking.repository.ParkingRepository;
import com.my.parking.repository.UserRepository;
import com.my.parking.util.MessageSenderUtil;
import com.my.parking.util.ReplyKeyboardMarkupUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

@Component
public class GetReservationListByParkingAndDateCommand implements Command {

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private ParkingPlaceRepository parkingPlaceRepository;

    @Autowired
    private ParkingRepository parkingRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void execute(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        long currentChatID = callbackQuery.getFrom().getId();
        User currentUser = userRepository.findById(currentChatID).orElse(null);
        ReplyKeyboardMarkup replyKeyboardMarkup = ReplyKeyboardMarkupUtil.createReplyKeyboardMarkup(currentUser);

        String[] callbackQueryData = callbackQuery.getData().split("_");
        long parkingId = Long.parseLong(callbackQueryData[1]);
        Date currentDate = Date.valueOf(callbackQueryData[2]);
        Parking currentParking = parkingRepository.findById(parkingId).orElse(null);

        Iterable<ParkingPlace> parkingPlaces = parkingPlaceRepository.findAllByParkingAndDate(currentParking, currentDate);
        StringBuilder stringBuilder;
        assert currentParking != null;

        if (StreamSupport.stream(parkingPlaces.spliterator(), false).findAny().isEmpty()) {
            stringBuilder = new StringBuilder("На паркінгу за адресою " + currentParking.getAddress().getName() + " немає бронювань на " + currentDate);
        } else {
            stringBuilder = new StringBuilder("На паркінгу за адресою " + currentParking.getAddress().getName() + " на " + currentDate + " є такі бронювання:\n\n");

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
