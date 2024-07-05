package domain.entity;

import domain.entity.vos.events.AcquireLockEventVO;
import domain.entity.vos.events.LockAcquiredEventVO;
import domain.enums.LockStatus;
import domain.entity.vos.lock.RunningDetailsVO;
import domain.event.EventPublisher;
import domain.event.impls.LockAcquiredEvent;
import domain.repository.PersistenceContext;
import lombok.Data;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Data
public class Lock {
    private String id;
    private Integer version;
    private LockStatus status;
    private Process process;
    private RunningDetailsVO runningDetailsVO;
    private Instant createdAt;
    private Instant updatedAt;
    private Date partitionDate;
    private Object context;
    private List<String> workingSet;

    public static Boolean isStopTheWorldRunning(PersistenceContext persistenceContext) {
        return persistenceContext.lockRepository().hasStopTheWorldLockOnRunningStatus();
    }

    public static void lockCreatedAcquireOpportunity(PersistenceContext persistenceContext, String lockId) {
        if (Boolean.TRUE.equals(Lock.isStopTheWorldRunning(persistenceContext))
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
        if (Boolean.TRUE.equals(Lock.isStopTheWorldRunning(persistenceContext))) {
            return;
        }

        var concurrentProcessesPending = persistenceContext.lockRepository().getPendingConcurrentProcessOrderedByPriority(lockId);
        if(concurrentProcessesPending.size() > 0) {
            concurrentProcessesPending.forEach(concurrentProcessPending -> {
                Lock.lockCreatedAcquireOpportunity(persistenceContext, concurrentProcessPending);
            });
        }
    }
}
