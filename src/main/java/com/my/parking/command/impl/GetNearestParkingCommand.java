package com.my.parking.command.impl;

import com.my.parking.command.Command;
import com.my.parking.messagesender.MessageSender;
import com.my.parking.model.Parking;
import com.my.parking.repository.ParkingPlaceRepository;
import com.my.parking.repository.ParkingRepository;
import com.my.parking.util.LocationUtil;
import com.my.parking.util.MessageSenderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class GetNearestParkingCommand implements Command {

    @Autowired
    private ParkingRepository parkingRepository;

    @Autowired
    private ParkingPlaceRepository parkingPlaceRepository;

    @Autowired
    private MessageSender messageSender;

    @Override
    public void execute(Update update) {
        Message message = update.getMessage();
        Long currentChatID = message.getChatId();

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        Iterable<Parking> parkingIterable = parkingRepository.findAll();
        List<Parking> parkingList = StreamSupport.stream(parkingIterable.spliterator(), false)
                .collect(Collectors.toList());

        Map<Parking, Double> nearestParking = new LinkedHashMap<>();

        for (int i = 0; i < 3; i++){
            Map.Entry<Parking, Double> parking = LocationUtil.getNearestParking(message.getLocation(), parkingList);
            nearestParking.put(parking.getKey(), parking.getValue());
            parkingList.remove(parking.getKey());
        }

        for (Map.Entry<Parking, Double> parkingEntry : nearestParking.entrySet()) {
            keyboard.add(Collections.singletonList(
                    InlineKeyboardButton.builder()
                            .text(parkingEntry.getKey().getAddress().getName()
                                    + String.format(" - %.2f грн"
                                            + " - %.2f км" +
                                            " - Рейтинг %.1f",
                                            parkingEntry.getKey().getPrice(),
                                            parkingEntry.getValue(),
                                            parkingPlaceRepository.getRatingByParkingId(parkingEntry.getKey().getId()).orElse(0.0)))
                            .callbackData("reserveDateForNearestParking_" + parkingEntry.getKey().getId())
                            .build()));
        }

        InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder()
                .keyboard(keyboard)
                .build();

        MessageSenderUtil.sendMessage("Це найближчі до вас паркінги, натисніть на нього, щоб обрати дату бронювання",
                currentChatID,
                inlineKeyboardMarkup,
                messageSender);

    }
}
