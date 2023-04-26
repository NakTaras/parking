package com.my.parking.model;

public class User {
    private long id;
    private String fullName;
    private String phoneNum;
    private Role role;
    private UserStatus userStatus;

    public User(long id) {
        this.id = id;
        this.role = Role.USER;
        this.userStatus = UserStatus.USER_ADDED;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public Role getRole() {
        return role;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }
}
