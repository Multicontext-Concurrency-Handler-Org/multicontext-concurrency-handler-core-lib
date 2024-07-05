package domain.enums;

import java.util.Arrays;

public enum LockLevel {
    PROCESS("process"),
    DATA("data");

    private final String code;

    public static LockLevel fromCode(String value) {
        return Arrays.stream(values())
                .filter(x -> x.code.equals(value))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("invalid code"));
    }

    LockLevel(String code) {
        this.code = code;
    }

    String getCode() {
        return this.code;
    }
}
