package com.my.parking.roleprocessor;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface RoleProcessor {
    void processMessage(Update update);
    void processCallBackQuery(Update update);
}
