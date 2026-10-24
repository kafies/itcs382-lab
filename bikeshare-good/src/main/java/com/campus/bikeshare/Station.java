package com.campus.bikeshare;

import java.util.Arrays;

public enum Station {
    LIBRARY, CANTEEN, DORM;

    public static Station parse(String text) {
        return Arrays.stream(values())
                .filter(s -> s.name().equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown station: " + text));
    }
}
