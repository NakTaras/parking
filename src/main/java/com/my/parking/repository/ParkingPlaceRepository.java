package com.my.parking.repository;

import com.my.parking.model.ParkingPlace;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ParkingPlaceRepository extends CrudRepository<ParkingPlace, Long> {
}
