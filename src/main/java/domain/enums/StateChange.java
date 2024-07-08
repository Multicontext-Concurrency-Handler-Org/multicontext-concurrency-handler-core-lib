package domain.enums;

import java.util.Arrays;

public enum StateChange {
    LOCK_CREATED("lock_created"),
    LOCK_RELEASED("lock_released");

    private final String code;

    StateChange(String code) {
        this.code = code;
    }

    public static StateChange fromCode(String value) {
        return Arrays.stream(values())
                .filter(x -> x.code.equals(value))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("invalid code"));
    }
    public String getCode() {
        return this.code;
    }
}
