package domain.error;

import domain.entity.vos.events.DomainErrorEventVO;
import domain.enums.DomainErrorType;
import domain.event.impls.DomainErrorEvent;
import lombok.AllArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
public class MissingConfigurationException extends DomainErrorException {
    private final String missingConfiguration;
    @Override
    protected DomainErrorEvent toEvent() {
        return new DomainErrorEvent(
                new DomainErrorEventVO(
                        DomainErrorType.MISSING_CONFIG,
                        String.format("Missing configuration: %s", this.missingConfiguration),
                        Instant.now()
                )
        );
    }
}
