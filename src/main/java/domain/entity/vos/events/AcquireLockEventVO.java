package domain.entity.vos.events;

import domain.entity.Lock;

import java.util.List;

public record AcquireLockEventVO(
        Integer version,
        String process,
        List<String> workingSet,
        Object payload
) {
    public static AcquireLockEventVO fromEntity(Lock acquiredLock) {
        return new AcquireLockEventVO(
                acquiredLock.getVersion(),
                acquiredLock.getProcess().getName(),
                acquiredLock.getWorkingSet(),
                acquiredLock.getContext()
        );
    }
}
