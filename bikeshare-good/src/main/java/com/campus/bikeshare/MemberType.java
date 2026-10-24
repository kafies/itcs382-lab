package com.campus.bikeshare;

import java.util.Arrays;

public enum MemberType {
    STUDENT(2),
    STAFF(3);

    private final int feePerMinute;

    MemberType(int feePerMinute) {
        this.feePerMinute = feePerMinute;
    }

    public int feePerMinute() {
        return feePerMinute;
    }

    public static MemberType parse(String text) {
        return Arrays.stream(values())
                .filter(t -> t.name().equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Member type must be student or staff."));
    }
}
