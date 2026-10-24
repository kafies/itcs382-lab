package com.campus.bikeshare;

import java.util.HashMap;
import java.util.Map;

public class BikeShare {
    private final BikeInventory inventory;
    private final RideRepository rides;
    private final Map<String, Rental> activeRentals = new HashMap<>();

    public BikeShare(BikeInventory inventory, RideRepository rides) {
        this.inventory = inventory;
        this.rides = rides;
    }

    public Rental rent(String memberId, MemberType type, Station station) {
        if (activeRentals.containsKey(memberId)) {
            throw new IllegalStateException("Member " + memberId + " already has a bike out.");
        }
        Bike bike = inventory.take(station)
                .orElseThrow(() -> new IllegalStateException("No bikes available at " + station + "."));
        Rental rental = new Rental(memberId, type, bike, station);
        activeRentals.put(memberId, rental);
        return rental;
    }

    public Ride giveBack(String memberId, Station station, int minutes) {
        if (minutes <= 0) {
            throw new IllegalArgumentException("Ride time must be at least 1 minute.");
        }
        Rental rental = activeRentals.remove(memberId);
        if (rental == null) {
            throw new IllegalStateException("Member " + memberId + " has no bike out.");
        }
        inventory.giveBack(station, rental.bike());
        Ride ride = new Ride(rental, station, minutes);
        rides.save(ride);
        return ride;
    }
}
