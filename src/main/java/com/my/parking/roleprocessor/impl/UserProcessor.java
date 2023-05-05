package com.my.parking.roleprocessor.impl;

import com.my.parking.command.Command;
import com.my.parking.command.UserCommandContainer;
import com.my.parking.command.impl.ChangeUserPhoneNumberCommand;
import com.my.parking.command.impl.GetNearestParkingCommand;
import com.my.parking.command.impl.UserRegistrationCommand;
import com.my.parking.command.impl.WrongRequestCommand;
import com.my.parking.enums.UserStatusEnum;
import com.my.parking.model.User;
import com.my.parking.repository.UserRepository;
import com.my.parking.roleprocessor.RoleProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class UserProcessor implements RoleProcessor {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRegistrationCommand userRegistrationCommand;

    @Autowired
    private WrongRequestCommand wrongRequestCommand;

    @Autowired
    private UserCommandContainer userCommandContainer;

    @Autowired
    private GetNearestParkingCommand getNearestParkingCommand;

    @Autowired
    private ChangeUserPhoneNumberCommand changeUserPhoneNumberCommand;

    @Override
    public void processMessage(Update update) {
        Message message = update.getMessage();
        String messageText = null;
        if (message.hasText()) {
            messageText = message.getText().split("\n")[0];
        }

        User user = userRepository
                .findById(message.getChatId())
                .orElse(null);

        if (user == null || !UserStatusEnum.USER_CREATED.name().equals(user.getUserStatus().getName())) {
            userRegistrationCommand.execute(update);
            return;
        }

        Command command = userCommandContainer.getCommand(messageText);

         if (message.hasLocation()) {
             getNearestParkingCommand.execute(update);
             return;
         }

         if (message.hasContact()) {
             changeUserPhoneNumberCommand.execute(update);
             return;
         }

        if (command != null) {
            command.execute(update);
        } else {
            wrongRequestCommand.execute(update);
        }
    }

    @Override
    public void processCallBackQuery(Update update) {

        //TODO: send list with parking and validate (when get request to reserve parking place) if parking has enough parking places

        CallbackQuery callbackQuery = update.getCallbackQuery();
        String data = callbackQuery.getData();

        Command command = userCommandContainer.getCommand(data.split("_")[0]);
        command.execute(update);

    }
}
