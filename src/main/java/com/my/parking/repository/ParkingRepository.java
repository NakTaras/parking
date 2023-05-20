package com.my.parking.repository;

import com.my.parking.model.Address;
import com.my.parking.model.Parking;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface ParkingRepository extends CrudRepository<Parking, Long> {
    @Query(value = "SELECT * FROM parking p WHERE p.number_of_parking_places > " +
            "(SELECT COUNT(*) FROM parking_place pp WHERE pp.parking_id = p.id AND pp.date = ?1)",
            nativeQuery = true)
    List<Parking> findAvailableParkingByDate(Date date);

    @Query(value = "SELECT * FROM parking p WHERE p.id = ?1 AND p.number_of_parking_places > " +
            "(SELECT COUNT(*) FROM parking_place pp WHERE pp.parking_id = p.id AND pp.date = ?2)",
            nativeQuery = true)
    Parking findParkingIfAvailableByDateAndId(long parkingId, Date date);

    @Query(value = "SELECT p.number_of_parking_places FROM parking p WHERE p.id = ?1",
            nativeQuery = true)
    Integer findNumberOfParkingPlaces(long parkingId);

    Iterable<Parking> findParkingByAddressAndPriceAndNumberOfParkingPlaces(Address address, double price, int numberOfParkingPlaces);
}
