package lib;

import lib.dto.ConsumeAcquireLockDTO;
import lib.dto.ConsumeDeadlockCleanupEventDTO;
import lib.dto.ConsumeReleaseLockDTO;
import lib.dto.ConsumeStateChangeEventDTO;
import lib.exceptions.MCHConstraintViolationException;

public interface IMCHCoreAPI {
    void consumeStateChange(ConsumeStateChangeEventDTO stateChangeEventDTO) throws MCHConstraintViolationException;
    void consumeAcquireLock(ConsumeAcquireLockDTO acquireLockDTO) throws MCHConstraintViolationException;
    void consumeReleaseLock(ConsumeReleaseLockDTO releaseLockDTO) throws MCHConstraintViolationException;
    void consumeDeadlockCleanupEvent(ConsumeDeadlockCleanupEventDTO deadlockCleanupEventDTO) throws MCHConstraintViolationException;
}
