package lib.usecase;

import lib.IMCHCore;
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
public class UseCasesFacade implements IMCHCore {
    private final AcquireOpportunityUseCase acquireOpportunityUseCase;
    private final AcquireLockRequestUseCase acquireLockRequestUseCase;
    private final ReleaseLockRequestUseCase releaseLockRequestUseCase;
    private final DeadlockCleanupUseCase deadlockCleanupUseCase;

    public void consumeStateChange(ConsumeStateChangeEventDTO stateChangeEventDTO) {
        acquireOpportunityUseCase.call(stateChangeEventDTO);
    }

    public void consumeAcquireLock(ConsumeAcquireLockDTO acquireLockDTO) {
        acquireLockRequestUseCase.call(acquireLockDTO);
    }

    public void consumeReleaseLock(ConsumeReleaseLockDTO releaseLockDTO) {
        releaseLockRequestUseCase.call(releaseLockDTO);
    }

    public void consumeDeadlockCleanupEvent(ConsumeDeadlockCleanupEventDTO deadlockCleanupEventDTO) {
        deadlockCleanupUseCase.call(deadlockCleanupEventDTO);
    }
}
