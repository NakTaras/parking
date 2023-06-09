package com.my.parking.util;

import com.my.parking.enums.RoleEnum;
import com.my.parking.enums.UserStatusEnum;
import com.my.parking.model.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class ReplyKeyboardMarkupUtil {

    private ReplyKeyboardMarkupUtil() {
    }

    public static ReplyKeyboardMarkup createReplyKeyboardMarkup(User user){

        if (user != null && RoleEnum.ADMIN.name().equals(user.getRole().getName())) {
            return createAdminReplyKeyboardMarkup();
        } else {
            return createUserReplyKeyboardMarkup(user);
        }
    }

    private static ReplyKeyboardMarkup createAdminReplyKeyboardMarkup() {
        var keyboardRow1 = new KeyboardRow();
        var keyboardRow2 = new KeyboardRow();
        var keyboardRow3 = new KeyboardRow();
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        keyboardRow1.add("Додати паркінг");
        keyboardRow1.add("Список паркінгів");
        keyboardRow2.add("Список паркінгів для оновлення даних");
        keyboardRow3.add("Список бронювань на паркінгу");
        keyboardRowList.add(keyboardRow1);
        keyboardRowList.add(keyboardRow2);
        keyboardRowList.add(keyboardRow3);
        return ReplyKeyboardMarkup.builder()
                .keyboard(keyboardRowList)
                .resizeKeyboard(true)
                .oneTimeKeyboard(true)
                .build();
    }

    private static ReplyKeyboardMarkup createUserReplyKeyboardMarkup(User user) {
        if (user == null) {
            return createRegistrationReplyKeyboardMarkup();
        } else if (UserStatusEnum.USER_ADDED.name().equals(user.getUserStatus().getName())) {
            return createContactReplyKeyboardMarkup();
        }
        return createUserReplyKeyboardMarkup();
    }

    private static ReplyKeyboardMarkup createUserReplyKeyboardMarkup() {
        var keyboardRow1 = new KeyboardRow();
        var keyboardRow2 = new KeyboardRow();
        var keyboardRow3 = new KeyboardRow();
        var keyboardRow4 = new KeyboardRow();
        var keyboardRow5 = new KeyboardRow();
        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        keyboardRow1.add("Список паркінгів");
        keyboardRow1.add(KeyboardButton.builder()
                .text("Знайти найближчий паркінг")
                .requestLocation(true)
                .build());
        keyboardRow2.add("Вибрати дату для бронювання");
        keyboardRow2.add("Скасувати бронювання");
        keyboardRow3.add("Змінити ім'я");
        keyboardRow3.add(KeyboardButton.builder()
                .text("Змінити номер телефону")
                .requestContact(true)
                .build());
        keyboardRow4.add("Список активних бронювань");
        keyboardRow4.add("Оцінити бронювання");
        keyboardRow5.add("Історія бронювань");

        keyboardRowList.add(keyboardRow1);
        keyboardRowList.add(keyboardRow2);
        keyboardRowList.add(keyboardRow3);
        keyboardRowList.add(keyboardRow4);
        keyboardRowList.add(keyboardRow5);

        return ReplyKeyboardMarkup.builder()
                .keyboard(keyboardRowList)
                .resizeKeyboard(true)
                .oneTimeKeyboard(true)
                .build();
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
