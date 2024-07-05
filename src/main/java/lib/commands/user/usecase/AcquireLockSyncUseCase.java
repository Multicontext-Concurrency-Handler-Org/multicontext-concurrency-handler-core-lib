package lib.commands.user.usecase;

import domain.repository.PersistenceContext;
import lib.commands.user.dto.AcquireLockSyncDTO;
import lib.commands.user.dto.LockAcquiredSyncDTO;
import lib.commands.user.errors.AcquireLockSyncFailedException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AcquireLockSyncUseCase {
    private final PersistenceContext persistenceContext;

    public LockAcquiredSyncDTO execute(AcquireLockSyncDTO request) throws AcquireLockSyncFailedException {
        throw new AcquireLockSyncFailedException(false);
    }
}
