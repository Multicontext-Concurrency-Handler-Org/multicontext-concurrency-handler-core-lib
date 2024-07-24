package domain.error;

import domain.entity.vos.events.DomainErrorEventVO;
import domain.enums.DomainErrorType;
import domain.event.impls.DomainErrorEvent;
import lombok.AllArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
public class InvalidStateException extends DomainErrorException {
    private final String invalidStateReason;
    @Override
    protected DomainErrorEvent toEvent() {
        return new DomainErrorEvent(
                new DomainErrorEventVO(
                        DomainErrorType.INVALID_STATE,
                        String.format("Invalid state: %s", this.invalidStateReason),
                        Instant.now()
                )
        );
    }
}
