package com.my.parking.processor;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface Processor {

    void process(Update update);
}
