package lib.dto;

import java.time.Instant;

public record ConsumeDeadlockCleanupEventDTO(
        Integer version,
        Instant lastCleanup
) { }
