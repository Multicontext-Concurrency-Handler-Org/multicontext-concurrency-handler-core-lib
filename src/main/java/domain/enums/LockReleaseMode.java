package domain.enums;

import java.util.Arrays;
import java.util.Optional;

public enum LockReleaseMode {
    EVENT("event"),
    AUTO("auto"),
    MANUAL("manual");

    private final String code;

    LockReleaseMode(String code) {
        this.code = code;
    }

    public static Optional<LockReleaseMode> fromCode(String value) {
        return Arrays.stream(values())
                .filter(x -> x.code.equals(value))
                .findFirst();
    }
    public String getCode() {
        return this.code;
    }
}
