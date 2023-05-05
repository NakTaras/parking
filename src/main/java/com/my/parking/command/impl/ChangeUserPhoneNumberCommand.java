package com.my.parking.command.impl;

import com.my.parking.command.Command;
import com.my.parking.messagesender.MessageSender;
import com.my.parking.model.User;
import com.my.parking.repository.UserRepository;
import com.my.parking.util.MessageSenderUtil;
import com.my.parking.util.ReplyKeyboardMarkupUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Component
public class ChangeUserPhoneNumberCommand implements Command {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageSender messageSender;

    @Override
    public void execute(Update update) {
        Message message = update.getMessage();
        Long currentChatID = message.getChatId();
        String newPhoneNumber = message.getContact().getPhoneNumber();
        User user = userRepository.findById(currentChatID).orElse(null);

        ReplyKeyboardMarkup replyKeyboardMarkup = ReplyKeyboardMarkupUtil.createReplyKeyboardMarkup(user);

        assert user != null;

        user.setPhoneNum(newPhoneNumber);
        userRepository.save(user);

        MessageSenderUtil.sendMessage("Ви успішно змінили номер телефону\n" +
                        "Новий номер телефону: " + user.getPhoneNum(),
                currentChatID,
                replyKeyboardMarkup,
                messageSender);
    }
}
