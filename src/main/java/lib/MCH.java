package lib;

import lib.exceptions.MCHConstraintViolationException;
import lib.usecase.impls.AcquireLockRequestUseCase;
import lib.usecase.impls.AcquireOpportunityUseCase;
import lib.usecase.impls.DeadlockCleanupUseCase;
import lib.usecase.impls.ReleaseLockRequestUseCase;
import lib.dto.ConsumeAcquireLockDTO;
import lib.dto.ConsumeDeadlockCleanupEventDTO;
import lib.dto.ConsumeReleaseLockDTO;
import lib.dto.ConsumeStateChangeEventDTO;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MCH implements IMulticontextConcurrencyHandler{
    private final AcquireOpportunityUseCase acquireOpportunityUseCase;
    private final AcquireLockRequestUseCase acquireLockRequestUseCase;
    private final ReleaseLockRequestUseCase releaseLockRequestUseCase;
    private final DeadlockCleanupUseCase deadlockCleanupUseCase;

    public void consumeStateChange(ConsumeStateChangeEventDTO stateChangeEventDTO) throws MCHConstraintViolationException {
        acquireOpportunityUseCase.call(stateChangeEventDTO);
    }

    public void consumeAcquireLock(ConsumeAcquireLockDTO acquireLockDTO) throws MCHConstraintViolationException {
        acquireLockRequestUseCase.call(acquireLockDTO);
    }

    public void consumeReleaseLock(ConsumeReleaseLockDTO releaseLockDTO) throws MCHConstraintViolationException {
        releaseLockRequestUseCase.call(releaseLockDTO);
    }

    public void consumeDeadlockCleanupEvent(ConsumeDeadlockCleanupEventDTO deadlockCleanupEventDTO) throws MCHConstraintViolationException {
        deadlockCleanupUseCase.call(deadlockCleanupEventDTO);
    }
}
