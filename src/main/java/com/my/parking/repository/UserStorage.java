package com.my.parking.repository;

import com.my.parking.model.User;

import java.util.HashMap;
import java.util.Map;

public class UserStorage {
    private static final Map<Long, User> users = new HashMap<>();

    public static Map<Long, User> getUsers() {
        return users;
    }
}
