package com.my.parking.processor.impl;

import com.my.parking.enums.RoleEnum;
import com.my.parking.model.User;
import com.my.parking.processor.Processor;
import com.my.parking.repository.UserRepository;
import com.my.parking.roleprocessor.impl.AdminProcessor;
import com.my.parking.roleprocessor.impl.UserProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class MessageProcessor implements Processor {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminProcessor adminProcessor;

    @Autowired
    private UserProcessor userProcessor;

    @Override
    public void process(Update update) {
        User user = userRepository.findById(update.getMessage().getChatId()).orElse(null);

        if (user != null && RoleEnum.ADMIN.name().equals(user.getRole().getName())) {
            adminProcessor.processMessage(update);
        } else {
            userProcessor.processMessage(update);
        }
    }
}
