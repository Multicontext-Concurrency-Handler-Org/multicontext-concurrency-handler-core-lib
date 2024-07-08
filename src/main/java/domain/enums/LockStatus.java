package domain.enums;

import java.util.Arrays;

public enum LockStatus {
    PENDING("pending"),
    RUNNING("running"),
    STOPPED("stopped");

    private final String code;
    LockStatus(String code) {
        this.code = code;
    }
    public static LockStatus fromCode(String value) {
        return Arrays.stream(values())
                .filter(x -> x.code.equals(value))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("invalid code"));
    }
    public String getCode() {
        return this.code;
    }
}
