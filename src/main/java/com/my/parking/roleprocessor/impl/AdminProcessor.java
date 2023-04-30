package com.my.parking.roleprocessor.impl;

import com.my.parking.command.AdminCommandContainer;
import com.my.parking.command.Command;
import com.my.parking.command.impl.WrongRequestCommand;
import com.my.parking.roleprocessor.RoleProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class AdminProcessor implements RoleProcessor {

    @Autowired
    private AdminCommandContainer adminCommandContainer;

    @Autowired
    private WrongRequestCommand wrongRequestCommand;

    @Override
    public void processMessage(Update update) {
        Message message = update.getMessage();
        String messageText = message.getText().split("\n")[0];

        Command command = adminCommandContainer.getCommand(messageText);

        if (command != null) {
            command.execute(update);
        } else {
            wrongRequestCommand.execute(update);
        }
    }

    @Override
    public void processCallBackQuery(Update update) {

    }
}
