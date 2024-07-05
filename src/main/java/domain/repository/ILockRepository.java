package domain.repository;

import domain.entity.Lock;

import java.util.List;

public interface ILockRepository {
    Boolean hasStopTheWorldLockOnRunningStatus();

    Boolean hasConcurrentProcessRunning(String lockId);

    Lock updateToRunning(String lockId);

    List<String> getPendingConcurrentProcessOrderedByPriority(String lockId);
}
