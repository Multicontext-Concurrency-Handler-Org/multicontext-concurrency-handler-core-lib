package domain.entity.vos.events;

public record ReleaseLockEventVO(
        String lockId,
        String process
) { }
