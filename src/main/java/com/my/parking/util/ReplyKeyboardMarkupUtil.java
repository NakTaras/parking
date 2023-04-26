package com.my.parking.util;

import com.my.parking.model.User;
import com.my.parking.model.UserStatus;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

public class ReplyKeyboardMarkupUtil {

    public static ReplyKeyboardMarkup createReplyKeyboardMarkup(User user){
        if (user == null) {
            return createRegistrationReplyKeyboardMarkup();
        } else if (user.getUserStatus().equals(UserStatus.USER_ADDED)) {
            return createContactReplyKeyboardMarkup();
        }
        return null;
    }

    private static ReplyKeyboardMarkup createRegistrationReplyKeyboardMarkup() {
        var button = new KeyboardRow();
        button.add("Зареєструватись");
        return ReplyKeyboardMarkup.builder()
                .keyboardRow(button)
                .resizeKeyboard(true)
                .oneTimeKeyboard(true)
                .build();
    }

    private static ReplyKeyboardMarkup createContactReplyKeyboardMarkup() {
        var button = new KeyboardRow();

        button.add(KeyboardButton.builder()
                .text("Відправити номер")
                .requestContact(true)
                .build());

        return ReplyKeyboardMarkup.builder()
                .keyboardRow(button)
                .resizeKeyboard(true)
                .oneTimeKeyboard(true)
                .build();
    }

}
