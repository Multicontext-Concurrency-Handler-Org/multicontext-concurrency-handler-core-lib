package lib;

import lib.commands.engine.dto.StateChangeEventDTO;
import lib.commands.user.dto.AcquireLockSyncDTO;
import lib.commands.user.dto.LockAcquiredSyncDTO;
import lib.commands.user.dto.SendAcquireLockEventDTO;
import lib.commands.user.dto.SendReleaseLockEventDTO;

public interface IMulticontextConcurrencyHandlerAPI {
    // Events Handler
    void sendAcquireLockEvent(SendAcquireLockEventDTO request);
    void sendReleaseLockEvent(SendReleaseLockEventDTO request);

    // Controller
    LockAcquiredSyncDTO acquireLockSync(AcquireLockSyncDTO request);

    // State Change Handler
    void executeAcquireOpportunity(StateChangeEventDTO request);
}
