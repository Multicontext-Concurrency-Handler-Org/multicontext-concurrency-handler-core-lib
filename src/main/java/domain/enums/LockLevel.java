package domain.enums;

import java.util.Arrays;
import java.util.Optional;

public enum LockLevel {
    PROCESS("process"),
    DATA("data");

    private final String code;

    public static Optional<LockLevel> fromCode(String value) {
        return Arrays.stream(values())
                .filter(x -> x.code.equals(value))
                .findFirst();
    }

    LockLevel(String code) {
        this.code = code;
    }

    String getCode() {
        return this.code;
    }
}
