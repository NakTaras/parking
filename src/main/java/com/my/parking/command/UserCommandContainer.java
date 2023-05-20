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

    @Autowired
    private ChooseDateForNearestParkingCommand chooseDateForNearestParkingCommand;

    @Autowired
    private NextDateForNearestParkingCommand nextDateForNearestParkingCommand;

    @Autowired
    private GetParkingListCommand getParkingListCommand;

    @Autowired
    private RequestChangeUserNameCommand requestChangeUserNameCommand;

    @Autowired
    private GetReservationsToCancelCommand getReservationsToCancelCommand;

    @Autowired
    private CancelReservationCommand cancelReservationCommand;

    @Autowired
    private ChangeUserNameCommand changeUserNameCommand;

    @Autowired
    private GetReservationListByUserCommand getReservationListByUserCommand;

    public Command getCommand(String commandName) {
        if (null == commands){
            commands = new HashMap<>();

            commands.put("reserveDate", getParkingListToReserveCommand);
            commands.put("Вибрати дату для бронювання", chooseDateCommand);
            commands.put("nextDate", nextDateCommand);
            commands.put("reserveParking", reserveParkingCommand);
            commands.put("reserveDateForNearestParking", chooseDateForNearestParkingCommand);
            commands.put("nextDateForNearestParking", nextDateForNearestParkingCommand);
            commands.put("Список паркінгів", getParkingListCommand);
            commands.put("Змінити ім'я", requestChangeUserNameCommand);
            commands.put("Нове ім'я", changeUserNameCommand);
            commands.put("Скасувати бронювання", getReservationsToCancelCommand);
            commands.put("cancelReservation", cancelReservationCommand);
            commands.put("Список активних бронювань", getReservationListByUserCommand);
        }
        return commands.get(commandName);
    }
}
