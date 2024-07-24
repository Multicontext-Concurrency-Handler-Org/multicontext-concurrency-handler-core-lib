package domain.enums;

import java.util.Arrays;
import java.util.Optional;

public enum DomainErrorType {
    INVALID_STATE("invalid_state"),
    MISSING_CONFIG("missing_config"),
    UNEXPECTED_EXCEPTION("unexpected_exception"),
    WRONG_ABSTRACTION_USAGE("wrong_abstraction_usage");

    private final String code;

    public static Optional<DomainErrorType> fromCode(String value) {
        return Arrays.stream(values())
                .filter(x -> x.code.equals(value))
                .findFirst();
    }

    DomainErrorType(String code) {
        this.code = code;
    }

    String getCode() {
        return this.code;
    }
}
