package com.my.parking.roleprocessor.impl;

import com.my.parking.command.impl.UserRegistrationCommand;
import com.my.parking.command.impl.WrongRequestCommand;
import com.my.parking.enums.UserStatusEnum;
import com.my.parking.model.User;
import com.my.parking.repository.UserRepository;
import com.my.parking.roleprocessor.RoleProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class UserProcessor implements RoleProcessor {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRegistrationCommand userRegistrationCommand;

    @Autowired
    private WrongRequestCommand wrongRequestCommand;

    @Override
    public void processMessage(Update update) {
        User user = userRepository
                .findById(update.getMessage().getChatId())
                .orElse(null);

        if (user == null || !UserStatusEnum.USER_CREATED.name().equals(user.getUserStatus().getName())) {
            userRegistrationCommand.execute(update);
            return;
        }

        wrongRequestCommand.execute(update);
    }

    @Override
    public void processCallBackQuery(Update update) {

    }
}
