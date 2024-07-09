package domain.enums;

import java.util.Arrays;
import java.util.Optional;

public enum LockStatus {
    PENDING("pending"),
    RUNNING("running"),
    STOPPED("stopped");

    private final String code;
    LockStatus(String code) {
        this.code = code;
    }
    public static Optional<LockStatus> fromCode(String value) {
        return Arrays.stream(values())
                .filter(x -> x.code.equals(value))
                .findFirst();
    }
    public String getCode() {
        return this.code;
    }
}
