package domain.errors;

import domain.entity.vos.events.DomainErrorEventVO;
import domain.enums.DomainErrorType;
import domain.event.impls.DomainErrorEvent;
import lombok.Builder;

import java.time.Instant;

public class LockNotFoundError extends Exception implements IDomainError {
    private final String lockIdNotFound;
    private final Instant occurredAt = Instant.now();

    public LockNotFoundError(String lockIdNotFound) {
        this.lockIdNotFound = lockIdNotFound;
    }

    @Override
    public DomainErrorEvent toEvent() {
        return new DomainErrorEvent(
                new DomainErrorEventVO(
                        DomainErrorType.INVALID_STATE,
                        String.format("Invalid state - expected to find lock id %s", this.lockIdNotFound),
                        this.occurredAt
                )
        );
    }
}
