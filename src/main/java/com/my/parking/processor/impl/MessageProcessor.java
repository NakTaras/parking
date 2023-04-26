package com.my.parking.processor.impl;

import com.my.parking.messagesender.MessageSender;
import com.my.parking.model.User;
import com.my.parking.model.UserStatus;
import com.my.parking.processor.Processor;
import com.my.parking.repository.UserStorage;
import com.my.parking.util.ReplyKeyboardMarkupUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

@Component
public class MessageProcessor implements Processor {

    @Autowired
    private MessageSender messageSender;

    //Todo: додати щось типу userCreationCommand і в залежності від статусу користувача буде виконуватись певна команда

    @Override
    public void process(Update update) {
        Message message = update.getMessage();
        Long currentChatID = message.getChatId();
        User user = UserStorage.getUsers().get(currentChatID);
        var replyKeyboardMarkup = ReplyKeyboardMarkupUtil.createReplyKeyboardMarkup(user);

        if (message.hasText()) {
            String messageText = message.getText();

            if (messageText.equals("Зареєструватись") && user == null) {
                UserStorage.getUsers().put(currentChatID, new User(currentChatID));
                sendMessage("Введіть свій ПІБ",
                        currentChatID,
                        //TODO: refactor ReplyKeyboardRemove
                        ReplyKeyboardRemove.builder().removeKeyboard(true).build());
                return;
            }

            if (user == null) {
                sendMessage("Вам потрібно зареєструватись",
                        currentChatID,
                        replyKeyboardMarkup);
                return;
            } else if (user.getUserStatus().equals(UserStatus.USER_ADDED)) {
                user.setFullName(messageText);
                user.setUserStatus(UserStatus.ADDED_USER_FULL_NAME);
                sendMessage("Відправте номер свого телефону",
                        currentChatID,
                        replyKeyboardMarkup);
                return;
            }

            if (messageText.equals("test")) {
                sendMessage("Люкс команда",
                        currentChatID,
                        replyKeyboardMarkup);
            } else {
                sendMessage("Ви ввели неправильну команду: " + messageText,
                        currentChatID,
                        replyKeyboardMarkup);
            }
        } else if (message.hasContact()) {
            if (user.getUserStatus().equals(UserStatus.ADDED_USER_FULL_NAME)) {
                user.setPhoneNum(message.getContact().getPhoneNumber());
                user.setUserStatus(UserStatus.USER_CREATED);

                sendMessage("Ви успішно зареєструвались" + System.lineSeparator()
                                + "Ваші дані:" + System.lineSeparator()
                                + "ПІБ: " + user.getFullName() + System.lineSeparator()
                                + "Номер телефону: " + user.getPhoneNum() + System.lineSeparator(),
                        currentChatID,
                        //TODO: refactor ReplyKeyboardRemove
                        ReplyKeyboardRemove.builder().removeKeyboard(true).build());
            } else {
                sendMessage("Ви ввели неправильну команду",
                        currentChatID,
                        replyKeyboardMarkup);
            }
        }
    }

    private void sendMessage(String messageText, long chatId, ReplyKeyboard replyKeyboard) {
        messageSender.sendMessage(
                SendMessage.builder()
                        .text(messageText)
                        .replyMarkup(replyKeyboard)
                        .chatId(chatId)
                        .build());
    }
}
