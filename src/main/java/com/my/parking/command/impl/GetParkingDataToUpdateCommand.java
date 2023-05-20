package com.my.parking.command.impl;

import com.my.parking.command.Command;
import com.my.parking.messagesender.MessageSender;
import com.my.parking.model.Parking;
import com.my.parking.model.User;
import com.my.parking.repository.ParkingRepository;
import com.my.parking.repository.UserRepository;
import com.my.parking.util.MessageSenderUtil;
import com.my.parking.util.ReplyKeyboardMarkupUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Component
public class GetParkingDataToUpdateCommand implements Command {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ParkingRepository parkingRepository;

    @Autowired
    private MessageSender messageSender;

    @Override
    public void execute(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        Long currentChatID = callbackQuery.getFrom().getId();
        String[] callbackQueryData = callbackQuery.getData().split("_");
        long parkingId = Long.parseLong(callbackQueryData[1]);
        User user = userRepository.findById(currentChatID).orElse(null);
        Parking parking = parkingRepository.findById(parkingId).orElse(null);

        ReplyKeyboardMarkup replyKeyboardMarkup = ReplyKeyboardMarkupUtil.createReplyKeyboardMarkup(user);

        assert parking != null;
        MessageSenderUtil.sendMessage("Скопіюйте текст натиснувши на нього та відредагуйте потрібні дані\n" +
                        "<code>Оновити паркінг\n" +
                        "ID паркінгу: " + parking.getId() + "\n" +
                        "Адреса: " + parking.getAddress().getName() + "\n" +
                        "Координати: " + parking.getAddress().getLatitude() + ";" + parking.getAddress().getLongitude() + "\n" +
                        "Ціна за паркомісце: " + String.format("%.2f грн", parking.getPrice()) + "\n" +
                        "Кількість паркомісць: " + parking.getNumberOfParkingPlaces() + "</code>",
                currentChatID,
                replyKeyboardMarkup,
                messageSender);
    }
}
