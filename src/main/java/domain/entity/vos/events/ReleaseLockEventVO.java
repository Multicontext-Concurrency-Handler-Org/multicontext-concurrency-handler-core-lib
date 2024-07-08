package domain.entity.vos.events;

public record ReleaseLockEventVO(
        Integer version,
        String lockId,
        String process
) { }
