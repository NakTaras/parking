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
public class ReservationHistoryCommand implements Command {
    @Autowired
    private MessageSender messageSender;

    @Autowired
    private ParkingPlaceRepository parkingPlaceRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void execute(Update update) {
        Long currentChatID = update.getMessage().getChatId();

        Date currentDate = Date.valueOf(LocalDate.now());
        User currentUser = userRepository.findById(currentChatID).orElse(null);

        Iterable<ParkingPlace> parkingPlaces = parkingPlaceRepository.findAllByUserAndDateIsLessThan(currentUser, currentDate);
        ReplyKeyboardMarkup replyKeyboardMarkup = ReplyKeyboardMarkupUtil.createReplyKeyboardMarkup(currentUser);
        if (StreamSupport.stream(parkingPlaces.spliterator(), false).findAny().isEmpty()) {

            MessageSenderUtil.sendMessage("У вас немає історії бронювань",
                    currentChatID,
                    replyKeyboardMarkup,
                    messageSender);
            return;
        }
        StringBuilder reservationHistory = new StringBuilder().append("Ваша історія бронювань").append(System.lineSeparator()).append(System.lineSeparator());
        int i = 1;

        for (ParkingPlace parkingPlace : parkingPlaces) {
            if (i > 10) {
                break;
            }

            reservationHistory.append("Номер резерву: ").append(parkingPlace.getId()).append(System.lineSeparator())
                    .append("Адреса паркінгу: ").append(parkingPlace.getParking().getAddress().getName()).append(System.lineSeparator())
                    .append("Дата бронювання: ").append(parkingPlace.getDate()).append(System.lineSeparator())
                    .append(String.format("Ціна за паркомісце: %.2f грн", parkingPlace.getParking().getPrice()))
                    .append(System.lineSeparator()).append(System.lineSeparator());

            i++;
        }


        MessageSenderUtil.sendMessage(reservationHistory.toString(),
                currentChatID,
                replyKeyboardMarkup,
                messageSender);
    }
}
