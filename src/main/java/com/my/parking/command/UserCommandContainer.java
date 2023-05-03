package com.my.parking.command;

import com.my.parking.command.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserCommandContainer {

    private Map<String, Command> commands;

    @Autowired
    private GetParkingListToReserveCommand getParkingListToReserveCommand;

    @Autowired
    private ChooseDateCommand chooseDateCommand;

    @Autowired
    private NextDateCommand nextDateCommand;

    @Autowired
    private ReserveParkingCommand reserveParkingCommand;

    public Command getCommand(String commandName) {
        if (null == commands){
            commands = new HashMap<>();

            commands.put("reserveDate", getParkingListToReserveCommand);
            commands.put("Вибрати дату для бронювання", chooseDateCommand);
            commands.put("nextDate", nextDateCommand);
            commands.put("reserveParking", reserveParkingCommand);
        }
        return commands.get(commandName);
    }
}
