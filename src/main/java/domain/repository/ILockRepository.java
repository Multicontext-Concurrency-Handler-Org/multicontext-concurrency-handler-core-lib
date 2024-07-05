package domain.repository;

import domain.entity.Lock;

import java.util.List;

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
     * lockId origin process concurrency list
     * <h4>Process Lock Level</h4>
     * if there is any concurrent process lock executing that has lock level of process it will return true
     * <h4>Data Lock Level</h4>
     * if there is any concurrent process lock executing that has lock level of process, it will search for
     * duplicate occurrences between the concurrent process locks working sets and the parameter lock working set
     *
     * @return      true if there is a concurrent lock running
     */
    Boolean hasConcurrentProcessRunning(String lockId);

    /**
     * Update status to running and calculates expiresAt based on lifetime configured on the process
     * <p/>
     * lock.expiresAt = now() + process.lifetime
     *
     * @return      The lock updated
     */
    Lock updateToRunning(String lockId);


    /**
     * Query pending lock requests where status equals to pending order by priority
     *
     * @return      The lock id list
     */
    List<String> getPendingConcurrentProcessOrderedByPriority(String lockId);
}
