package com.campus.bikeshare;

public record Rental(String memberId, MemberType memberType, Bike bike, Station fromStation) {
}
