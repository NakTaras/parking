package com.my.parking.command.impl;

import com.my.parking.command.Command;
import com.my.parking.messagesender.MessageSender;
import com.my.parking.model.Parking;
import com.my.parking.model.User;
import com.my.parking.repository.ParkingPlaceRepository;
import com.my.parking.repository.ParkingRepository;
import com.my.parking.repository.UserRepository;
import com.my.parking.util.MessageSenderUtil;
import com.my.parking.util.ReplyKeyboardMarkupUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Component
public class GetParkingListCommand implements Command {

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private ParkingRepository parkingRepository;

    @Autowired
    private ParkingPlaceRepository parkingPlaceRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void execute(Update update) {
        Long currentChatID = update.getMessage().getChatId();
        User user = userRepository.findById(currentChatID).orElse(null);
        ReplyKeyboardMarkup replyKeyboardMarkup = ReplyKeyboardMarkupUtil.createReplyKeyboardMarkup(user);

        Iterable<Parking> parkingList = parkingRepository.findAll();
        StringBuilder parkingListInfo = new StringBuilder();
        int i = 1;

        for (Parking parking : parkingList) {
            if (i != 1) {
                parkingListInfo.append(System.lineSeparator()).append(System.lineSeparator());
            }

            Double parkingRating = parkingPlaceRepository.getRatingByParkingId(parking.getId()).orElse(0.0);
            parkingListInfo.append(i).append(". ").append(parking.getAddress().getName())
                    .append(System.lineSeparator())
                    .append(String.format("Ціна за паркомісце: %.2f грн", parking.getPrice()))
                    .append(System.lineSeparator())
                    .append("Кількість паркомісць: ")
                    .append(parking.getNumberOfParkingPlaces())
                    .append(System.lineSeparator())
                    .append(String.format("Рейтинг: %.1f", parkingRating));
            i++;
        }

        MessageSenderUtil.sendMessage(parkingListInfo.toString(),
                currentChatID,
                replyKeyboardMarkup,
                messageSender);
    }
}
