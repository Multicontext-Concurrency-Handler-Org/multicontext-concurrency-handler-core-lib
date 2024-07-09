package domain.enums;

import java.util.Arrays;
import java.util.Optional;

public enum DomainEventType {
    ACQUIRE_LOCK("acquire_lock"),
    RELEASE_LOCK("release_lock"),
    STATE_CHANGE("state_change"),
    LOCK_ACQUIRED("lock_acquired"),
    DOMAIN_ERROR("domain_error");
    private final String code;

    public static Optional<DomainEventType> fromCode(String value) {
        return Arrays.stream(values())
                .filter(x -> x.code.equals(value))
                .findFirst();
    }

    DomainEventType(String code) {
        this.code = code;
    }

    String getCode() {
        return this.code;
    }
}
