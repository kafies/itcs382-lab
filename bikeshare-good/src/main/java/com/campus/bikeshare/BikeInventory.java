package com.campus.bikeshare;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

public class BikeInventory {
    private static final int BIKES_PER_STATION = 2;

    private final Map<Station, Deque<Bike>> available = new EnumMap<>(Station.class);

    public BikeInventory() {
        for (Station station : Station.values()) {
            Deque<Bike> bikes = new ArrayDeque<>();
            for (int i = 1; i <= BIKES_PER_STATION; i++) {
                bikes.add(new Bike(station.name() + "-" + i));
            }
            available.put(station, bikes);
        }
    }

    public Optional<Bike> take(Station station) {
        return Optional.ofNullable(available.get(station).pollFirst());
    }

    public void giveBack(Station station, Bike bike) {
        available.get(station).addLast(bike);
    }
}
