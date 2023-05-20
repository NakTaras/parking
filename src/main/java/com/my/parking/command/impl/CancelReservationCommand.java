package com.my.parking.command.impl;

import com.my.parking.command.Command;
import com.my.parking.messagesender.MessageSender;
import com.my.parking.model.User;
import com.my.parking.repository.ParkingPlaceRepository;
import com.my.parking.repository.UserRepository;
import com.my.parking.util.MessageSenderUtil;
import com.my.parking.util.ReplyKeyboardMarkupUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.Objects;

@Component
public class CancelReservationCommand implements Command {
    @Autowired
    private MessageSender messageSender;

    @Autowired
    private ParkingPlaceRepository parkingPlaceRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void execute(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        Long currentChatID = callbackQuery.getFrom().getId();
        String[] callbackQueryData = callbackQuery.getData().split("_");
        long parkingPlaceId = Long.parseLong(callbackQueryData[1]);
        User user = userRepository.findById(currentChatID).orElse(null);
        ReplyKeyboardMarkup replyKeyboardMarkup = ReplyKeyboardMarkupUtil.createReplyKeyboardMarkup(user);

        parkingPlaceRepository.delete(Objects.requireNonNull(parkingPlaceRepository.findById(parkingPlaceId).orElse(null)));

        MessageSenderUtil.sendMessage("Ви успішно скасували бронювання",
                currentChatID,
                replyKeyboardMarkup,
                messageSender);
    }
}
