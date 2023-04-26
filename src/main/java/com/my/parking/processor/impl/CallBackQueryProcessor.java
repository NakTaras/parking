//package com.my.parking.processor.impl;
//
//import com.telegram.bot.command.Command;
//import com.telegram.bot.command.CommandContainer;
//import com.telegram.bot.processor.Processor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
//import org.telegram.telegrambots.meta.api.objects.Update;
//
//@Component
//public class CallBackQueryProcessor implements Processor {
//
//    @Autowired
//    private CommandContainer commandContainer;
//
//    @Override
//    public void process(Update update) {
//        CallbackQuery callbackQuery = update.getCallbackQuery();
//        String data = callbackQuery.getData();
//
//        Command command = commandContainer.getCommand(data.replaceAll("[0-9]", ""));
//        command.execute(update);
//    }
//}
