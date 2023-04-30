package com.my.parking.command;

import com.my.parking.command.impl.AddParkingCommand;
import com.my.parking.command.impl.CreateParkingCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AdminCommandContainer {

    private Map<String, Command> commands;

    @Autowired
    private AddParkingCommand addParkingCommand;

    @Autowired
    private CreateParkingCommand createParkingCommand;

    public Command getCommand(String commandName) {
        if (null == commands){
            commands = new HashMap<>();

            commands.put("Додати паркінг", addParkingCommand);
            commands.put("Створити паркінг", createParkingCommand);
        }
        return commands.get(commandName);
    }
}
