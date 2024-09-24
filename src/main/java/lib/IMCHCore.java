package lib;

import lib.dto.ConsumeAcquireLockDTO;
import lib.dto.ConsumeDeadlockCleanupEventDTO;
import lib.dto.ConsumeReleaseLockDTO;
import lib.dto.ConsumeStateChangeEventDTO;

public interface IMCHCore {
    void consumeStateChange(ConsumeStateChangeEventDTO stateChangeEventDTO);
    void consumeAcquireLock(ConsumeAcquireLockDTO acquireLockDTO);
    void consumeReleaseLock(ConsumeReleaseLockDTO releaseLockDTO);
    void consumeDeadlockCleanupEvent(ConsumeDeadlockCleanupEventDTO deadlockCleanupEventDTO);
}
