package domain.entity.vos.events;

import domain.entity.Lock;
import domain.enums.LockReleaseMode;

public record ReleaseLockEventVO(
        Integer version,
        String lockId,
        String process,
        LockReleaseMode releaseMode
) {
    public static ReleaseLockEventVO fromEntity(Lock lock) {
        return new ReleaseLockEventVO(
                lock.getVersion(),
                lock.getId(),
                lock.getProcess().getName(),
                lock.getRunningDetailsVO().releaseDetailsVO().releaseMode()
        );
    }
}
