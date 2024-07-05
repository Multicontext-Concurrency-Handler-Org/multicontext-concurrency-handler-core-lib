package domain.enums;

import java.util.Arrays;

public enum LockReleaseMode {
    EVENT("event"),
    AUTO("auto"),
    MANUAL("manual");

    private final String code;

    LockReleaseMode(String code) {
        this.code = code;
    }

    public static LockReleaseMode fromCode(String value) {
        return Arrays.stream(values())
                .filter(x -> x.code.equals(value))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("invalid code"));
    }
    public String getCode() {
        return this.code;
    }
}
