package domain.services;

import domain.entity.Lock;
import domain.entity.vos.events.AcquireLockEventVO;
import domain.entity.vos.events.LockAcquiredEventVO;
import domain.event.EventPublisher;
import domain.event.impls.LockAcquiredEvent;
import domain.error.InvalidStateException;
import domain.repository.PersistenceContext;
import lombok.AllArgsConstructor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static cross.MCHWrongAbstractionUsage.requireNonNullWithReason;

@AllArgsConstructor
public class LockService {
    private static final Logger logger = LogManager.getLogger();

    private final PersistenceContext persistenceContext;

    public String generateLockId() {
        return this.persistenceContext.lockRepository().generateLockId();
    }

    public void lockCreatedAcquireOpportunity(String lockId) {
        if (Boolean.TRUE.equals(this.persistenceContext.lockRepository().hasStopTheWorldLockOnRunningStatus())) {
            logger.debug("Won't be able to start running since there is a STW lock running");
            return;
        }

        var lockOpt = this.persistenceContext.lockRepository().findLockById(lockId);
        if(lockOpt.isEmpty()) {
            logger.error(String.format("No lock was found by id %s", lockId));
            return;
        }

        this.executeAcquireOpportunityFor(lockOpt.get());
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

        var concurrentLocksPending = getPendingConcurrentLocksOrderedByPriority(lock.get());
        requireNonNullWithReason(concurrentLocksPending, "LockService.getPendingConcurrentLocksOrderedByPriority(Lock lock) should never return null");
        concurrentLocksPending.forEach(this::executeAcquireOpportunityFor);
    }

    private void executeAcquireOpportunityFor(Lock lock) {
        var concurrentProcesses = this.persistenceContext.processRepository().getConcurrentProcesses(lock.getProcess());
        if(Objects.isNull(concurrentProcesses)) {
            logger.warn("Unexpected behaviour IProcessRepository.getConcurrentProcesses(Process process) returned null instead of an empty list");
            concurrentProcesses = new ArrayList<>();
        }

        final var closureParameterConcurrentProcess = concurrentProcesses;
        this.persistenceContext.executeWithinAccessExclusiveLockContext(() -> {
            if(Boolean.TRUE.equals(persistenceContext.lockRepository().hasConcurrentProcessRunning(lock, closureParameterConcurrentProcess))) {
                logger.debug("Won't be able to start running since there is a concurrent process running");
                return;
            }

            lock.startRunning();
            persistenceContext.lockRepository().upsert(lock);
            logger.info(String.format("Lock %s started running", lock.getId()));

            var acquireLockEventVO = AcquireLockEventVO.fromEntity(lock);
            var lockAcquiredEventVO = new LockAcquiredEventVO(lock.getId(), acquireLockEventVO);
            var lockAcquiredEvent = new LockAcquiredEvent(lockAcquiredEventVO);

            EventPublisher.publishEvent(lockAcquiredEvent);
            logger.info(String.format("Lock %s lock acquired event published", lock.getId()));
        });
    }

    private List<Lock> getPendingConcurrentLocksOrderedByPriority(Lock lock) {
        if(Boolean.TRUE.equals(lock.getProcess().getIsStopTheWorld())) {
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

    private List<Lock> getAllPendingLocksOrderedByPriority() {
        var concurrentLocksPending = this.persistenceContext.lockRepository().getAllPendingLocksOrderedByPriority();
        if(Objects.isNull(concurrentLocksPending)) {
            logger.warn("Unexpected behaviour ILockRepository.getAllPendingLocksOrderedByPriority() returned null instead of an empty list");
            return new ArrayList<>();
        }

        return concurrentLocksPending;
    }

    public void upsert(Lock lock) {
        this.persistenceContext.lockRepository().upsert(lock);
    }

    public Lock findLockByIdOrThrow(String lockId) throws InvalidStateException {
        var lockOpt = this.persistenceContext.lockRepository().findLockById(lockId);

        if(lockOpt.isEmpty()) {
            throw new InvalidStateException(String.format("lock %s not found by id", lockId));
        }

        return lockOpt.get();
    }
}
