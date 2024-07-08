package lib.dto;

public record ConsumeReleaseLockDTO(
        Integer version,
        String lockId,
        String process
) {
}
