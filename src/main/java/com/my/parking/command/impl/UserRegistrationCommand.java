package com.my.parking.command.impl;

import com.my.parking.command.Command;
import com.my.parking.enums.RoleEnum;
import com.my.parking.enums.UserStatusEnum;
import com.my.parking.messagesender.MessageSender;
import com.my.parking.model.User;
import com.my.parking.repository.RoleRepository;
import com.my.parking.repository.UserRepository;
import com.my.parking.repository.UserStatusRepository;
import com.my.parking.util.MessageSenderUtil;
import com.my.parking.util.ReplyKeyboardMarkupUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

@Component
public class UserRegistrationCommand implements Command {
    @Autowired
    private MessageSender messageSender;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserStatusRepository userStatusRepository;

    @Override
    public void execute(Update update) {
        Message message = update.getMessage();
        Long currentChatID = message.getChatId();
        User user = userRepository.findById(currentChatID).orElse(null);
        ReplyKeyboardMarkup replyKeyboardMarkup = ReplyKeyboardMarkupUtil.createReplyKeyboardMarkup(user);

        if (message.hasText()) {
            String messageText = message.getText();

            if (messageText.equals("Зареєструватись") && user == null) {
                User newUser = User.builder()
                        .id(currentChatID)
                        .fullName("unknown_name")
                        .phoneNum("unknown_phone")
                        .role(roleRepository.findRoleByName(RoleEnum.USER.name()))
                        .userStatus(userStatusRepository.findUserStatusByName(UserStatusEnum.USER_ADDED.name()))
                        .build();
                userRepository.save(newUser);
                MessageSenderUtil.sendMessage("Введіть свій ПІБ",
                        currentChatID,
                        //TODO: refactor ReplyKeyboardRemove
                        ReplyKeyboardRemove.builder().removeKeyboard(true).build(),
                        messageSender);
                return;
            }

            if (user == null) {
                MessageSenderUtil.sendMessage("Вам потрібно зареєструватись",
                        currentChatID,
                        replyKeyboardMarkup,
                        messageSender);
                return;
            } else if (UserStatusEnum.USER_ADDED.name().equals(user.getUserStatus().getName())) {
                user.setFullName(messageText);
                user.setUserStatus(
                        userStatusRepository.findUserStatusByName(
                                UserStatusEnum.ADDED_USER_FULL_NAME.name()));
                userRepository.save(user);
                MessageSenderUtil.sendMessage("Відправте номер свого телефону",
                        currentChatID,
                        replyKeyboardMarkup,
                        messageSender);
                return;
            }

            MessageSenderUtil.sendMessage("Ви ввели неправильну команду",
                    currentChatID,
                    replyKeyboardMarkup,
                    messageSender);

        } else if (message.hasContact() && user != null) {
            if (UserStatusEnum.ADDED_USER_FULL_NAME.name().equals(user.getUserStatus().getName())) {
                user.setPhoneNum(message.getContact().getPhoneNumber());
                user.setUserStatus(
                        userStatusRepository.findUserStatusByName(
                                UserStatusEnum.USER_CREATED.name()));
                userRepository.save(user);

                MessageSenderUtil.sendMessage("Ви успішно зареєструвались" + System.lineSeparator()
                                + "Ваші дані:" + System.lineSeparator()
                                + "ПІБ: " + user.getFullName() + System.lineSeparator()
                                + "Номер телефону: " + user.getPhoneNum() + System.lineSeparator(),
                        currentChatID,
                        //TODO: refactor ReplyKeyboardRemove
                        ReplyKeyboardRemove.builder().removeKeyboard(true).build(),
                        messageSender);
            } else {
                MessageSenderUtil.sendMessage("Ви ввели неправильну команду",
                        currentChatID,
                        replyKeyboardMarkup,
                        messageSender);
            }
        }
    }
}
