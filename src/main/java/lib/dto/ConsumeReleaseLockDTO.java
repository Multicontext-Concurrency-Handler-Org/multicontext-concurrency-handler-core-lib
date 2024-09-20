package lib.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ConsumeReleaseLockDTO(
        @NotNull @Positive Integer version,
        @NotNull @NotEmpty @Size(min=32, max=32) String lockId,
        @NotNull @NotEmpty @Size(max=200) String process
) {
}
