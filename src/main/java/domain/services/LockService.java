package domain.services;

import domain.entity.Lock;
import domain.entity.vos.events.AcquireLockEventVO;
import domain.entity.vos.events.LockAcquiredEventVO;
import domain.event.EventPublisher;
import domain.event.impls.LockAcquiredEvent;
import domain.repository.PersistenceContext;

public class LockService {
    public static void lockCreatedAcquireOpportunity(PersistenceContext persistenceContext, String lockId) {
        if (Boolean.TRUE.equals(persistenceContext.lockRepository().hasStopTheWorldLockOnRunningStatus())
                || Boolean.TRUE.equals(persistenceContext.lockRepository().hasConcurrentProcessRunning(lockId))) {
            return;
        }

        Lock acquiredLock = persistenceContext.lockRepository().updateToRunning(lockId);

        var acquireLockEventVO = new AcquireLockEventVO(acquiredLock.getVersion(), acquiredLock.getProcess().getName(),
                acquiredLock.getProcess().getNotificationTarget(), acquiredLock.getWorkingSet(), acquiredLock.getContext());

        var lockAcquiredEventVO = new LockAcquiredEventVO(lockId, acquireLockEventVO);
        var lockAcquiredEvent = new LockAcquiredEvent(lockAcquiredEventVO);

        EventPublisher.publishEvent(lockAcquiredEvent);
    }

    public static void lockReleasedAcquireOpportunity(PersistenceContext persistenceContext, String lockId) {
        if (Boolean.TRUE.equals(persistenceContext.lockRepository().hasStopTheWorldLockOnRunningStatus())) {
            return;
        }

        var concurrentProcessesPending = persistenceContext.lockRepository().getPendingConcurrentProcessOrderedByPriority(lockId);
        if (concurrentProcessesPending.size() > 0) {
            concurrentProcessesPending.forEach(concurrentProcessPending -> {
                LockService.lockCreatedAcquireOpportunity(persistenceContext, concurrentProcessPending);
            });
        }
    }
}
