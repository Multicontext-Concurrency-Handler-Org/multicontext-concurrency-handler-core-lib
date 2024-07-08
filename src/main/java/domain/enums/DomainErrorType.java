package domain.enums;

import java.util.Arrays;

public enum DomainErrorType {
    INVALID_STATE("invalid_state");

    private final String code;

    public static DomainErrorType fromCode(String value) {
        return Arrays.stream(values())
                .filter(x -> x.code.equals(value))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("invalid code"));
    }

    DomainErrorType(String code) {
        this.code = code;
    }

    String getCode() {
        return this.code;
    }
}
