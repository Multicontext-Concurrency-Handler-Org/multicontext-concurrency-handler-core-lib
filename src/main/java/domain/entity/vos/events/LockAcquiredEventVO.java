package domain.entity.vos.events;

public record LockAcquiredEventVO(
        String lockId,
        AcquireLockEventVO requestEvent
) { }
