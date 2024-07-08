package lib.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import java.time.Instant;

public record ConsumeDeadlockCleanupEventDTO(
        @NotNull @Positive Integer version,
        @NotNull @Past Instant lastCleanup
) { }
