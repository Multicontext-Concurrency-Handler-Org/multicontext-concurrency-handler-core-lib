package domain.repository;

import domain.entity.Lock;
import domain.entity.Process;

import java.util.List;
import java.util.Optional;

public interface ILockRepository {
    /**
     * Query if there's any lock on the database where
     * status equals to running and stopTheWorld property equals to true
     *
     * @return      true if there is a stopTheWorld lock running
     */
    Boolean hasStopTheWorldLockOnRunningStatus();

    /**
     * Query if there's any lock on the database where
     * status equals to running and process is part of the
     * lock origin process concurrency list
     * <h4>Process Lock Level</h4>
     * if there is any concurrent process lock executing that has lock level of process it will return true
     * <h4>Data Lock Level</h4>
     * if there is any concurrent process lock executing that has lock level of process, it will search for
     * duplicate occurrences between the concurrent process locks working sets and the parameter lock working set
     *
     * @return      true if there is a concurrent lock running
     */
    Boolean hasConcurrentProcessRunning(Lock lock);

    /**
     * Find lock
     *
     * @return      The found lock
     */
    Optional<Lock> findLockById(String lockId);

    /**
     * Upsert lock
     *
     * @param lock
     */
    void upsert(Lock lock);


    /**
     * Query locks where status equals to pending order by priority
     *
     * @return      The lock id list
     */
    List<Lock> getConcurrentLocksPendingOrderedByPriority(List<Process> concurrentProcesses);

    /**
     * Get all locks with status equal pending order by priority
     *
     * @return      All pending locks order by priority
     */
    List<Lock> getAllPendingLocksOrderedByPriority();
}
