package com.my.parking.repository;

import com.my.parking.model.Address;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AddressRepository extends CrudRepository<Address, Long> {
    Address findAddressByName(String name);
}
