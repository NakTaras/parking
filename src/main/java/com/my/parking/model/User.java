package com.my.parking.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
public class User {

    @Id
    private long id;

    @NonNull
    private String fullName;

    @NonNull
    private String phoneNum;

    @NonNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Role role;

    @NonNull
    @ManyToOne(fetch = FetchType.EAGER)
    private UserStatus userStatus;

//    public User(long id) {
//        this.id = id;
//        this.role = Role.USER;
//        this.userStatus = UserStatus.USER_ADDED;
//        this.fullName = "unknown_name";
//        this.phoneNum = "unknown_phone";
//    }

    public void setId(long id) {
        this.id = id;
    }

    public void setFullName(@NonNull String fullName) {
        this.fullName = fullName;
    }

    public void setPhoneNum(@NonNull String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setRole(@NonNull Role role) {
        this.role = role;
    }

    public void setUserStatus(@NonNull UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public long getId() {
        return id;
    }

    public @NonNull String getFullName() {
        return fullName;
    }

    public @NonNull String getPhoneNum() {
        return phoneNum;
    }

    public @NonNull Role getRole() {
        return role;
    }

    public @NonNull UserStatus getUserStatus() {
        return userStatus;
    }
}
