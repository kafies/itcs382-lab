package com.campus.bikeshare;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class CsvRideRepository implements RideRepository {
    private static final String HEADER =
            "member_id,member_type,bike,from_station,return_station,minutes,fee";

    private final Path file;

    public CsvRideRepository(Path file) {
        this.file = file;
    }

    @Override
    public void save(Ride ride) {
        try {
            boolean writeHeader = Files.notExists(file) || Files.size(file) == 0;
            try (BufferedWriter out = Files.newBufferedWriter(file, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                if (writeHeader) {
                    out.write(HEADER);
                    out.newLine();
                }
                out.write(toCsv(ride));
                out.newLine();
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Could not save ride to " + file, e);
        }
    }

    private static String toCsv(Ride ride) {
        Rental rental = ride.rental();
        return String.join(",",
                rental.memberId(),
                rental.memberType().name(),
                rental.bike().id(),
                rental.fromStation().name(),
                ride.returnStation().name(),
                String.valueOf(ride.minutes()),
                String.valueOf(ride.fee()));
    }
}
