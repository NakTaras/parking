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
@Table(name = "parking")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
public class Parking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private double price;

    private int numberOfParkingPlaces;

    @NonNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Address address;
}
