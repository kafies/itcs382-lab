package bikeshare;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    public static final String[] STATIONS = {"Library", "Canteen", "Dorm"};
    public static final int STUDENT = 1;
    public static final int STAFF = 2;
    public static final String RIDE_FILE = "rides.csv";
    public static Map<String, String> bikeStation = new LinkedHashMap<>();
    public static Map<String, String> renter = new HashMap<>();
    public static Map<String, Integer> renterType = new HashMap<>();

    public static void main(String[] args) {
        for (String s : STATIONS) {
            bikeStation.put(s + "-1", s);
            bikeStation.put(s + "-2", s);
        }
        try (Scanner in = new Scanner(System.in)) {
            while (true) {
                System.out.print("Command (rent/return/quit): ");
                String cmd = in.nextLine().trim().toLowerCase();
                if (cmd.equals("quit")) break;
                if (cmd.equals("rent")) rent(in);
                else if (cmd.equals("return")) giveBack(in);
                else report("Unknown command: " + cmd, true);
            }
        } catch (NoSuchElementException ex) {
            report("Input ended.", true);
        }
        report("Goodbye.", false);
    }

    static void rent(Scanner in) {
        System.out.print("Member ID: ");
        String member = in.nextLine().trim();
        System.out.print("Member type (student/staff): ");
        int type = typeCode(in.nextLine().trim());
        System.out.print("Station (Library/Canteen/Dorm): ");
        String station = findStation(in.nextLine().trim());
        if (type == 0) { report("Unknown member type.", true); return; }
        if (station == null) { report("Unknown station.", true); return; }
        if (bikeOf(member) != null) { report(member + " already has a bike.", true); return; }
        String bike = freeBikeAt(station);
        if (bike == null) { report("No bike left at " + station + ".", true); return; }
        bikeStation.put(bike, "");
        renter.put(bike, member);
        renterType.put(bike, type);
        report("Rented " + bike + " from " + station + ".", false);
    }

    static void giveBack(Scanner in) {
        System.out.print("Member ID: ");
        String member = in.nextLine().trim();
        String bike = bikeOf(member);
        if (bike == null) { report("No bike is rented by " + member + ".", true); return; }
        System.out.print("Return station (Library/Canteen/Dorm): ");
        String station = findStation(in.nextLine().trim());
        System.out.print("Ride minutes: ");
        int minutes = parseMinutes(in.nextLine().trim());
        if (station == null) { report("Unknown station.", true); return; }
        if (minutes < 0) { report("Minutes must be a whole number, zero or more.", true); return; }
        int type = renterType.get(bike);
        int fee = minutes * (type == STUDENT ? 2 : 3);
        bikeStation.put(bike, station);
        renter.remove(bike);
        renterType.remove(bike);
        report("Receipt: bike " + bike + ", " + minutes + " min, fee " + fee + " baht.", false);
        saveRide(bike, member, type, minutes, fee, station);
    }

    static int typeCode(String text) {
        if (text.equalsIgnoreCase("student")) return STUDENT;
        if (text.equalsIgnoreCase("staff")) return STAFF;
        return 0;
    }

    static String findStation(String name) {
        for (String s : STATIONS) {
            if (s.equalsIgnoreCase(name)) return s;
        }
        return null;
    }

    static String freeBikeAt(String station) {
        for (Map.Entry<String, String> e : bikeStation.entrySet()) {
            if (e.getValue().equals(station)) return e.getKey();
        }
        return null;
    }

    static String bikeOf(String member) {
        for (Map.Entry<String, String> e : renter.entrySet()) {
            if (e.getValue().equals(member)) return e.getKey();
        }
        return null;
    }

    static int parseMinutes(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    static void saveRide(String bike, String member, int type, int minutes, int fee, String station) {
        boolean isNew = !new File(RIDE_FILE).exists();
        try (FileWriter out = new FileWriter(RIDE_FILE, true)) {
            if (isNew) out.write("bike,member,type,minutes,fee,return_station\n");
            out.write(bike + "," + member + "," + type + "," + minutes + "," + fee + "," + station + "\n");
        } catch (IOException ex) {
            report("Could not save ride: " + ex.getMessage(), true);
        }
    }

    static void report(String message, boolean isError) {
        if (isError) System.err.println(message);
        else System.out.println(message);
    }
}
