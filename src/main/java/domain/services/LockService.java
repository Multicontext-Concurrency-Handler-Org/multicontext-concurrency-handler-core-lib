package domain.services;

import domain.entity.Lock;
import domain.entity.vos.events.AcquireLockEventVO;
import domain.entity.vos.events.LockAcquiredEventVO;
import domain.event.EventPublisher;
import domain.event.impls.LockAcquiredEvent;
import domain.repository.PersistenceContext;
import lombok.AllArgsConstructor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class LockService {
    private static final Logger logger = LogManager.getLogger();

    private final PersistenceContext persistenceContext;

    public void lockCreatedAcquireOpportunity(String lockId) {
        if (Boolean.TRUE.equals(this.persistenceContext.lockRepository().hasStopTheWorldLockOnRunningStatus())) {
            logger.debug("Won't be able to start running since there is a STW lock running");
            return;
        }

        if(Boolean.TRUE.equals(persistenceContext.lockRepository().hasConcurrentProcessRunning(lockId))) {
            logger.debug("Won't be able to start running since there is a concurrent process running");
            return;
        }

        Lock acquiredLock = this.persistenceContext.lockRepository().updateToRunning(lockId);
        logger.info(String.format("Lock %s started running", lockId));

        var acquireLockEventVO = AcquireLockEventVO.fromEntity(acquiredLock);
        var lockAcquiredEventVO = new LockAcquiredEventVO(lockId, acquireLockEventVO);
        var lockAcquiredEvent = new LockAcquiredEvent(lockAcquiredEventVO);

        EventPublisher.publishEvent(lockAcquiredEvent);
        logger.info(String.format("Lock %s lock acquired event published", lockId));
    }

    public void lockReleasedAcquireOpportunity(String lockId) {
        if (Boolean.TRUE.equals(this.persistenceContext.lockRepository().hasStopTheWorldLockOnRunningStatus())) {
            logger.debug("No lock will be able to start running since there is a STW lock running");
            return;
        }

        var lock = this.persistenceContext.lockRepository().findLockById(lockId);
        if(lock.isEmpty()) {
            logger.error(String.format("No lock was found by id %s", lockId));
            return;
        }

        List<String> concurrentLocksPending = getPendingConcurrentLocksOrderedByPriority(lock.get());
        Objects.requireNonNull(concurrentLocksPending, "LockService.getPendingConcurrentLocksOrderedByPriority(Lock lock) should never return null");
        concurrentLocksPending.forEach(this::lockCreatedAcquireOpportunity);
    }

    private List<String> getPendingConcurrentLocksOrderedByPriority(Lock lock) {
        if(lock.getProcess().getIsStopTheWorld()) {
            logger.trace("STW processes don't need concurrency filtering");
            return getAllPendingLocksOrderedByPriority();
        }

        var concurrentProcesses = this.persistenceContext.processRepository().getConcurrentProcesses(lock.getProcess());
        if(Objects.isNull(concurrentProcesses)) {
            logger.warn("Unexpected behaviour IProcessRepository.getConcurrentProcesses(Process process) returned null instead of an empty list");
            return new ArrayList<>();
        }

        var concurrentLocksPending = this.persistenceContext.lockRepository().getConcurrentLocksPendingOrderedByPriority(concurrentProcesses);
        if(Objects.isNull(concurrentLocksPending)) {
            logger.warn("Unexpected behaviour ILockRepository.getConcurrentLocksPendingOrderedByPriority(List<Process> concurrentProcesses) returned null instead of an empty list");
            return new ArrayList<>();
        }

        return concurrentLocksPending;
    }

    private List<String> getAllPendingLocksOrderedByPriority() {
        var concurrentLocksPending = this.persistenceContext.lockRepository().getAllPendingLocksOrderedByPriority();
        if(Objects.isNull(concurrentLocksPending)) {
            logger.warn("Unexpected behaviour ILockRepository.getAllPendingLocksOrderedByPriority() returned null instead of an empty list");
            return new ArrayList<>();
        }

        return concurrentLocksPending;
    }
}
