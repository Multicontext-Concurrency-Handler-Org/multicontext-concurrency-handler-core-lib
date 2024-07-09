package utils.repositories;

import domain.entity.Lock;
import domain.entity.Process;
import domain.enums.LockStatus;
import domain.repository.ILockRepository;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryLockRepository implements ILockRepository {
    private final List<Lock> locks = new ArrayList<>();

    public void add(Lock lock) {
        this.locks.add(lock);
    }

    @Override
    public Boolean hasStopTheWorldLockOnRunningStatus() {
        return this.locks.stream().anyMatch(lock -> lock.getStatus().equals(LockStatus.RUNNING) && lock.getProcess().getIsStopTheWorld());
    }

    @Override
    public Boolean hasConcurrentProcessRunning(Lock lock, List<String> concurrentProcesses) {
        return this.locks.stream().anyMatch(l ->
                l.getStatus().equals(LockStatus.RUNNING)
                        && concurrentProcesses.stream().anyMatch(
                        cp -> cp.equals(l.getProcess().getName())
                )
        );
    }

    @Override
    public Optional<Lock> findLockById(String lockId) {
        return this.locks.stream().findAny().filter(lock -> lock.getId().equals(lockId));
    }

    @Override
    public void upsert(Lock lock) {
        var lockOpt = this.findLockById(lock.getId());
        if (lockOpt.isEmpty()) {
            this.locks.add(lock);
        }
        // else there's no need to do anything cause its already in memory
    }

    @Override
    public List<Lock> getConcurrentLocksPendingOrderedByPriority(List<String> concurrentProcesses) {
        return this.locks.stream().filter(
                        lock -> lock.getStatus().equals(LockStatus.RUNNING)
                                && concurrentProcesses.stream().anyMatch(
                                cP -> cP.equals(lock.getProcess().getName())
                        )
                ).sorted(Comparator.comparingInt(l -> l.getProcess().getPriorityLevel()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Lock> getAllPendingLocksOrderedByPriority() {
        return this.locks.stream()
                .sorted(Comparator.comparingInt(l -> l.getProcess().getPriorityLevel()))
                .collect(Collectors.toList());
    }

    @Override
    public void acquireAccessExclusiveLock() {

    }

    @Override
    public void releaseAccessExclusiveLock() {

    }

    @Override
    public String generateLockId() {
        return UUID.randomUUID().toString();
    }
}
