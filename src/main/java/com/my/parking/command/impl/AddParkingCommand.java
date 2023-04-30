package com.my.parking.command.impl;

import com.my.parking.command.Command;
import com.my.parking.messagesender.MessageSender;
import com.my.parking.model.User;
import com.my.parking.repository.UserRepository;
import com.my.parking.util.MessageSenderUtil;
import com.my.parking.util.ReplyKeyboardMarkupUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Component
public class AddParkingCommand implements Command {
    @Autowired
    private MessageSender messageSender;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void execute(Update update) {
        Long currentChatID = update.getMessage().getChatId();
        User user = userRepository.findById(currentChatID).orElse(null);
        ReplyKeyboardMarkup replyKeyboardMarkup = ReplyKeyboardMarkupUtil.createReplyKeyboardMarkup(user);

        MessageSenderUtil.sendMessage("Скопіюйте текст натиснувши на нього та замініть квадратні дужки на необхідні значення\n" +
                        "<code>Створити паркінг\n" +
                        "Адреса: [адреса]\n" +
                        "Координати: [широта];[довгота]\n" +
                        "Ціна за паркомісце: [ціна]\n" +
                        "Кількість паркомісць: [кількість]</code>",
                currentChatID,
                replyKeyboardMarkup,
                messageSender);
    }
}
