package domain.entity.vos.events;

import domain.enums.DomainErrorType;

import java.time.Instant;

public record DomainErrorEventVO(
        DomainErrorType id,
        String message,
        Instant occurredAt
) { }
