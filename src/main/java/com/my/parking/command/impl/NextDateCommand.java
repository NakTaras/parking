package com.my.parking.command.impl;

import com.my.parking.command.Command;
import com.my.parking.messagesender.MessageSender;
import com.my.parking.util.MessageSenderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class NextDateCommand implements Command {

    @Autowired
    private MessageSender messageSender;

    @Override
    public void execute(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        Long currentChatID = callbackQuery.getFrom().getId();
        String lastDate = callbackQuery.getData().split("_")[1];

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        LocalDate date = LocalDate.parse(lastDate);

        for (int i = 1; i <= 7; i++) {
            keyboard.add(
                    Collections.singletonList(
                            InlineKeyboardButton.builder()
                                    .text(String.valueOf(date))
                                    .callbackData("reserveDate_" + date)
                                    .build()));
            date = date.plusDays(1);
        }

        keyboard.add(
                Collections.singletonList(
                        InlineKeyboardButton.builder()
                                .text("Далі")
                                .callbackData("nextDate_" + date)
                                .build()));

        InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder()
                .keyboard(keyboard)
                .build();

        MessageSenderUtil.sendMessage("Оберіть потрібну дату",
                currentChatID,
                inlineKeyboardMarkup,
                messageSender);
    }
}
