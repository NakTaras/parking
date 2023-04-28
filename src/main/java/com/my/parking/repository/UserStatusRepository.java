package com.my.parking.repository;

import com.my.parking.model.UserStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserStatusRepository extends CrudRepository<UserStatus, Long> {
    UserStatus findUserStatusByName(String name);
}
