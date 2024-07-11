package domain.repository;

import domain.entity.Lock;

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
    Boolean hasConcurrentProcessRunning(Lock lock, List<String> concurrentProcesses);

    /**
     * Find lock
     *
     * @return      The found lock
     */
    Optional<Lock> findLockById(String lockId);

    /**
     * Upsert definition: (computing, database) To insert rows into a database table if they do not already exist, or update them if they do.
     *
     * @param lock  The lock to be updated or created
     */
    void upsert(Lock lock);


    /**
     * Query locks where status equals to pending order by priority
     *
     * @return      The lock id list
     */
    List<Lock> getConcurrentLocksPendingOrderedByPriority(List<String> concurrentProcesses);

    /**
     * Get all locks with status equal pending order by priority
     *
     * @return      All pending locks order by priority
     */
    List<Lock> getAllPendingLocksOrderedByPriority();

    /**
     * Must lock the "lock table / collection" so that no other instance or threads can
     * neither query, update nor create new values to it until this lock is released
     * <p></p>
     * AccessExclusiveLock must have the same functioning as it is in postgresql
     * <a href="https://www.postgresql.org/docs/current/explicit-locking.html">PostgreSQL Locks Documentation</a>
     */
    void acquireAccessExclusiveLock();

    /**
     * Must unlock the "lock table / collection" so that other instances or threads can
     * query, update and create new values to it since this lock was released
     * <p></p>
     * AccessExclusiveLock must have the same functioning as it is in postgresql
     * <a href="https://www.postgresql.org/docs/current/explicit-locking.html">PostgreSQL Locks Documentation</a>
     */
    void releaseAccessExclusiveLock();

    /**
     * Return new lock id
     *
     * @return      New unused lock id
     */
    String generateLockId();
}
