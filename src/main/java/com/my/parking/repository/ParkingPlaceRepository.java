package com.my.parking.repository;

import com.my.parking.model.Parking;
import com.my.parking.model.ParkingPlace;
import com.my.parking.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.Optional;


@Repository
public interface ParkingPlaceRepository extends CrudRepository<ParkingPlace, Long> {
    @Query(value = "SELECT parking.number_of_parking_places - COUNT(*) FROM parking_place " +
            "INNER JOIN parking ON parking_place.parking_id = parking.id " +
            "WHERE parking_id = ?1 AND date = ?2",
            nativeQuery = true)
    Integer getAmountOfAvailableParkingPlaces(long parkingId, Date date);

    Iterable<ParkingPlace> findAllByUserAndDateGreaterThanEqual(User user, Date date);
    Iterable<ParkingPlace> findAllByUserAndDateIsLessThanAndRatingIsNull(User user, Date date);
    Iterable<ParkingPlace> findAllByUserAndDateIsLessThan(User user, Date date);
    Iterable<ParkingPlace> findAllByParkingAndDate(Parking parking, Date date);

    @Query(value = "SELECT SUM(parking_place.rating)/COUNT(parking_place.rating) FROM parking_place WHERE parking_id = ?1",
            nativeQuery = true)
    Optional<Double> getRatingByParkingId(long parkingId);
}
