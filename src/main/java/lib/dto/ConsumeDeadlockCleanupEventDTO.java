package lib.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;

import java.time.Instant;

public record ConsumeDeadlockCleanupEventDTO(
        @NotNull @Positive Integer version,
        @NotNull @Past Instant lastCleanup
) { }
