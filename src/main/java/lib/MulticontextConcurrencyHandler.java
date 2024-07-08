package lib;

import domain.event.IEventSubscriber;
import lib.commands.engine.dto.StateChangeEventDTO;
import lib.commands.engine.usecase.AcquireOpportunityUseCase;
import lib.commands.user.dto.AcquireLockSyncDTO;
import lib.commands.user.dto.LockAcquiredSyncDTO;
import lib.commands.user.dto.SendAcquireLockEventDTO;
import lib.commands.user.dto.SendReleaseLockEventDTO;
import lib.commands.user.usecase.AcquireLockSyncUseCase;
import lib.commands.user.usecase.SendAcquireLockEventUseCase;
import domain.repository.PersistenceContext;
import lib.commands.user.usecase.SendReleaseLockEventUseCase;
import lombok.AllArgsConstructor;

@AllArgsConstructor
class MulticontextConcurrencyHandler implements IMulticontextConcurrencyHandlerAPI {
    private final SendAcquireLockEventUseCase sendAcquireLockEventUseCase;
    private final SendReleaseLockEventUseCase sendReleaseLockEventUseCase;
    private final AcquireLockSyncUseCase acquireLockSyncUseCase;
    private final AcquireOpportunityUseCase acquireOpportunityUseCase;

    @Override
    public void sendAcquireLockEvent(SendAcquireLockEventDTO request) {
        this.sendAcquireLockEventUseCase.execute(request);
    }

    @Override
    public void sendReleaseLockEvent(SendReleaseLockEventDTO request) {
        this.sendReleaseLockEventUseCase.execute(request);
    }

    @Override
    public LockAcquiredSyncDTO acquireLockSync(AcquireLockSyncDTO request) {
        return this.acquireLockSyncUseCase.execute(request);
    }

    @Override
    public void executeAcquireOpportunity(StateChangeEventDTO request) {
        this.acquireOpportunityUseCase.execute(request);
    }
}
