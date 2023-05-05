package com.my.parking.command.impl;

import com.my.parking.command.Command;
import com.my.parking.enums.ParkingPlaceStatusEnum;
import com.my.parking.messagesender.MessageSender;
import com.my.parking.model.Parking;
import com.my.parking.model.ParkingPlace;
import com.my.parking.model.User;
import com.my.parking.repository.ParkingPlaceRepository;
import com.my.parking.repository.ParkingPlaceStatusRepository;
import com.my.parking.repository.ParkingRepository;
import com.my.parking.repository.UserRepository;
import com.my.parking.util.MessageSenderUtil;
import com.my.parking.util.ReplyKeyboardMarkupUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.sql.Date;

@Component
public class ReserveParkingCommand implements Command {

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private ParkingPlaceRepository parkingPlaceRepository;

    @Autowired
    private ParkingPlaceStatusRepository parkingPlaceStatusRepository;

    @Autowired
    private ParkingRepository parkingRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void execute(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        Long currentChatID = callbackQuery.getFrom().getId();
        String[] callbackQueryData = callbackQuery.getData().split("_");
        long parkingId = Long.parseLong(callbackQueryData[1]);
        Date date = Date.valueOf(callbackQueryData[2]);
        User user = userRepository.findById(currentChatID).orElse(null);
        ReplyKeyboardMarkup replyKeyboardMarkup = ReplyKeyboardMarkupUtil.createReplyKeyboardMarkup(user);

        Parking parking = parkingRepository.findParkingIfAvailableByDateAndId(parkingId, date);

        if (parking == null) {
            MessageSenderUtil.sendMessage("На даному паркінгу вже немає вільних паркомісць",
                    currentChatID,
                    replyKeyboardMarkup,
                    messageSender);
            return;
        }

        ParkingPlace parkingPlace = ParkingPlace.builder()
                .parking(parking)
                .parkingPlaceStatus(parkingPlaceStatusRepository.findParkingPlaceStatusByName(ParkingPlaceStatusEnum.RESERVED.name()))
                .date(date)
                .user(user)
                .build();

        parkingPlaceRepository.save(parkingPlace);

        MessageSenderUtil.sendMessage("Ви зарезервували паркомісце на паркінгу за адресою " + parking.getAddress().getName() + System.lineSeparator()
                        + "Дата резерву: " + parkingPlace.getDate() + System.lineSeparator()
                        + String.format("Ціна за паркомісце: %.2f грн", parking.getPrice()) + System.lineSeparator()
                        + "Номер резерву: " + parkingPlace.getId(),
                currentChatID,
                replyKeyboardMarkup,
                messageSender);
    }
}
