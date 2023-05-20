package com.my.parking.command.impl;

import com.my.parking.command.Command;
import com.my.parking.messagesender.MessageSender;
import com.my.parking.model.Address;
import com.my.parking.model.Parking;
import com.my.parking.model.User;
import com.my.parking.repository.AddressRepository;
import com.my.parking.repository.ParkingRepository;
import com.my.parking.repository.UserRepository;
import com.my.parking.util.MessageSenderUtil;
import com.my.parking.util.ReplyKeyboardMarkupUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class UpdateParkingCommand implements Command {

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ParkingRepository parkingRepository;

    @Override
    public void execute(Update update) {
        Message message = update.getMessage();
        Long currentChatID = message.getChatId();
        User user = userRepository.findById(currentChatID).orElse(null);
        ReplyKeyboardMarkup replyKeyboardMarkup = ReplyKeyboardMarkupUtil.createReplyKeyboardMarkup(user);

        Map<String, String> parkingData = new HashMap<>();

        String[] messageTestLines = message.getText().split("\n");
        Arrays.stream(messageTestLines)
                .filter(messageTestLine -> messageTestLine.contains(":"))
                .forEach(messageTestLine -> parkingData.put(messageTestLine.split(":")[0], messageTestLine.split(":")[1].trim()));


        Parking parking = parkingRepository.findById(Long.parseLong(parkingData.get("ID паркінгу"))).orElse(null);
        if (parking == null) {
            MessageSenderUtil.sendMessage("Паркінгу з таким ID не існує",
                currentChatID,
                replyKeyboardMarkup,
                messageSender);
            return;
        }

        Address address = parking.getAddress();
        address.setLatitude(Double.parseDouble(parkingData.get("Координати").split(";")[0]));
        address.setLongitude(Double.parseDouble(parkingData.get("Координати").split(";")[1]));
        address.setName(parkingData.get("Адреса"));
        addressRepository.save(address);

        parking.setNumberOfParkingPlaces(Integer.parseInt(parkingData.get("Кількість паркомісць")));
        parking.setPrice(Double.parseDouble(parkingData.get("Ціна за паркомісце").replaceAll("([а-яА-Я ]*)", "").replace(",", ".")));

        parkingRepository.save(parking);

        MessageSenderUtil.sendMessage("Ви успішно оновили інформацію про паркінг",
                currentChatID,
                replyKeyboardMarkup,
                messageSender);

//        if (address == null) {
//            address = Address.builder()
//                    .name(parkingData.get("Адреса"))
//                    .latitude(Double.parseDouble(parkingData.get("Координати").split(";")[0]))
//                    .longitude(Double.parseDouble(parkingData.get("Координати").split(";")[1]))
//                    .build();
//
//            addressRepository.save(address);
//        }
//
//        Parking parking = Parking.builder()
//                .address(address)
//                .numberOfParkingPlaces(Integer.parseInt(parkingData.get("Кількість паркомісць")))
//                .price(Double.parseDouble(parkingData.get("Ціна за паркомісце")))
//                .build();
//
//        parkingRepository.save(parking);
//
//        MessageSenderUtil.sendMessage("Ви успішно створили паркінг",
//                currentChatID,
//                replyKeyboardMarkup,
//                messageSender);
    }
}
