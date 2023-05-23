package com.my.parking.command;

import com.my.parking.command.impl.*;
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

    @Autowired
    private GetParkingListToUpdateDataCommand getParkingListToUpdateDataCommand;

    @Autowired
    private GetParkingDataToUpdateCommand getParkingDataToUpdateCommand;

    @Autowired
    private UpdateParkingCommand updateParkingCommand;

    @Autowired
    private GetParkingListToGetReservationCommand getParkingListToGetReservationCommand;

    @Autowired
    private ChooseDateToGetReservationsCommand chooseDateToGetReservationsCommand;

    @Autowired
    private NextDateForReservationsCommand nextDateForReservationsCommand;

    @Autowired
    private GetReservationListByParkingAndDateCommand getReservationListByParkingAndDateCommand;

    @Autowired
    private GetParkingListCommand getParkingListCommand;

    public Command getCommand(String commandName) {
        if (null == commands){
            commands = new HashMap<>();

            commands.put("Додати паркінг", addParkingCommand);
            commands.put("Створити паркінг", createParkingCommand);
            commands.put("Список паркінгів для оновлення даних", getParkingListToUpdateDataCommand);
            commands.put("updateParking", getParkingDataToUpdateCommand);
            commands.put("Оновити паркінг", updateParkingCommand);
            commands.put("Список бронювань на паркінгу", getParkingListToGetReservationCommand);
            commands.put("chooseDate", chooseDateToGetReservationsCommand);
            commands.put("nextDateForReservations", nextDateForReservationsCommand);
            commands.put("getReservations", getReservationListByParkingAndDateCommand);
            commands.put("Список паркінгів", getParkingListCommand);
        }
        return commands.get(commandName);
    }
}
