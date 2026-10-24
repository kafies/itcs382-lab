package com.campus.bikeshare;

import java.io.UncheckedIOException;
import java.util.Locale;
import java.util.Scanner;

public class Menu {
    private final BikeShare bikeShare;
    private final Scanner in;

    public Menu(BikeShare bikeShare, Scanner in) {
        this.bikeShare = bikeShare;
        this.in = in;
    }

    public void run() {
        System.out.println("BikeShare. Commands: rent, return, quit.");
        while (in.hasNextLine()) {
            System.out.print("> ");
            String command = in.nextLine().trim().toLowerCase(Locale.ROOT);
            try {
                switch (command) {
                    case "rent" -> rent();
                    case "return" -> giveBack();
                    case "quit" -> {
                        System.out.println("Goodbye.");
                        return;
                    }
                    default -> System.out.println("Unknown command. Use rent, return or quit.");
                }
            } catch (IllegalArgumentException | IllegalStateException | UncheckedIOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void rent() {
        String memberId = askMemberId();
        MemberType type = MemberType.parse(ask("Member type (student/staff): "));
        Station station = Station.parse(ask("Station (Library/Canteen/Dorm): "));
        Rental rental = bikeShare.rent(memberId, type, station);
        System.out.println("Rented bike " + rental.bike().id() + " at " + station + ".");
    }

    private void giveBack() {
        String memberId = askMemberId();
        Station station = Station.parse(ask("Return station (Library/Canteen/Dorm): "));
        int minutes = parseMinutes(ask("Ride time in minutes: "));
        printReceipt(bikeShare.giveBack(memberId, station, minutes));
    }

    private void printReceipt(Ride ride) {
        System.out.println("----- Receipt -----");
        System.out.println("Bike:    " + ride.rental().bike().id());
        System.out.println("Minutes: " + ride.minutes());
        System.out.println("Fee:     " + ride.fee() + " baht");
        System.out.println("-------------------");
    }

    private String askMemberId() {
        String id = ask("Member ID: ");
        if (id.isEmpty() || id.contains(",")) {
            throw new IllegalArgumentException("Member ID must be non-empty and contain no commas.");
        }
        return id;
    }

    private static int parseMinutes(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ride time must be a whole number of minutes.");
        }
    }

    private String ask(String prompt) {
        System.out.print(prompt);
        if (!in.hasNextLine()) {
            throw new IllegalStateException("Input ended.");
        }
        return in.nextLine().trim();
    }
}
