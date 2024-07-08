package lib.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public record ConsumeReleaseLockDTO(
        @NotNull @Positive Integer version,
        @NotNull @NotEmpty @Size(min=32, max=32) String lockId,
        @NotNull @NotEmpty @Size(max=200) String process
) {
}
