package com.my.parking.command.impl;

import com.my.parking.command.Command;
import com.my.parking.messagesender.MessageSender;
import com.my.parking.model.Parking;
import com.my.parking.repository.ParkingRepository;
import com.my.parking.util.MessageSenderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class GetParkingListToUpdateDataCommand implements Command {

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private ParkingRepository parkingRepository;

    @Override
    public void execute(Update update) {
        Long currentChatID = update.getMessage().getChatId();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        Iterable<Parking> parkingList = parkingRepository.findAll();

        parkingList.forEach(parking -> keyboard.add(Collections.singletonList(
                InlineKeyboardButton.builder()
                        .text(parking.getAddress().getName())
                        .callbackData("updateParking_" + parking.getId())
                        .build())));


        InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder()
                .keyboard(keyboard)
                .build();

        MessageSenderUtil.sendMessage("Оберіть бронювання, щоб оновити дані",
                currentChatID,
                inlineKeyboardMarkup,
                messageSender);
    }
}
