package com.my.parking.util;

import com.my.parking.model.Parking;
import org.telegram.telegrambots.meta.api.objects.Location;


public class LocationUtil {
    private LocationUtil() {

    }

    public static Parking getNearestParking(Location currentLocation, Iterable<Parking> parkingList) {
        double currentLat = currentLocation.getLatitude();
        double currentLon = currentLocation.getLongitude();
        double parkingLat;
        double parkingLon;
        int earthRad = 6371;
        double distance = Double.MAX_VALUE;
        Parking nearestParking = null;
        for (Parking parking : parkingList) {
            parkingLat = parking.getAddress().getLatitude();
            parkingLon = parking.getAddress().getLongitude();

            double dLat = deg2rad(parkingLat-currentLat);  // deg2rad below
            double dLon = deg2rad(parkingLon-currentLon);

            double a =
                    Math.sin(dLat/2) * Math.sin(dLat/2) +
                            Math.cos(deg2rad(currentLat)) * Math.cos(deg2rad(parkingLat)) *
                                    Math.sin(dLon/2) * Math.sin(dLon/2)
                    ;
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
            double tempDistance = earthRad * c;

            if (tempDistance < distance) {
                distance = tempDistance;
                nearestParking = parking;
            }
        }
        return nearestParking;
    }

    private static double deg2rad(double deg) {
        return deg * (Math.PI/180);
    }
}
