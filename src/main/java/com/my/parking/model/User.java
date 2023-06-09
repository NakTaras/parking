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
}
