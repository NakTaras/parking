package com.my.parking.roleprocessor.impl;

import com.my.parking.roleprocessor.RoleProcessor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class AdminProcessor implements RoleProcessor {
    @Override
    public void processMessage(Update update) {

    }

    @Override
    public void processCallBackQuery(Update update) {

    }
}
