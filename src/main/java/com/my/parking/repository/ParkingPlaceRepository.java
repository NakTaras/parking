package com.my.parking.repository;

import com.my.parking.model.ParkingPlace;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;


@Repository
public interface ParkingPlaceRepository extends CrudRepository<ParkingPlace, Long> {
    @Query(value = "SELECT parking.number_of_parking_places - COUNT(*) FROM parking_place " +
            "INNER JOIN parking ON parking_place.parking_id = parking.id " +
            "WHERE parking_id = ?1 AND date = ?2",
            nativeQuery = true)
    Integer getAmountOfAvailableParkingPlaces(long parkingId, Date date);
}
