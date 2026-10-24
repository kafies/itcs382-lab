package com.campus.bikeshare;

import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        BikeShare bikeShare = new BikeShare(
                new BikeInventory(),
                new CsvRideRepository(Path.of("rides.csv")));
        new Menu(bikeShare, new Scanner(System.in)).run();
    }
}
