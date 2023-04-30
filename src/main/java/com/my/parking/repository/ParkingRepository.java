package com.my.parking.repository;

import com.my.parking.model.Parking;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ParkingRepository extends CrudRepository<Parking, Long> {
}
