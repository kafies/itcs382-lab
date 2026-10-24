package com.campus.bikeshare;

public record Ride(Rental rental, Station returnStation, int minutes) {

    public int fee() {
        return minutes * rental.memberType().feePerMinute();
    }
}
