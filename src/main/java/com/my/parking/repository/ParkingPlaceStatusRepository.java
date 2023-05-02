package com.my.parking.repository;

import com.my.parking.model.ParkingPlaceStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ParkingPlaceStatusRepository extends CrudRepository<ParkingPlaceStatus, Long> {
    ParkingPlaceStatus findParkingPlaceStatusByName(String name);
}
