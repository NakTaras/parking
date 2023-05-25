package com.my.parking.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "parking_place")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
public class ParkingPlace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    private Date date;

    private Integer rating;

    @NonNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Parking parking;

    @NonNull
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

}
