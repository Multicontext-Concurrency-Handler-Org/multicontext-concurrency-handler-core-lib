package lib.commands.engine.usecase;

import domain.entity.Lock;
import domain.repository.PersistenceContext;
import lib.commands.engine.dto.StateChangeEventDTO;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AcquireOpportunityUseCase {
    private final PersistenceContext persistenceContext;

    public void execute(StateChangeEventDTO request) {
        switch (request.event()) {
            case LOCK_CREATED -> {
                request.locks().forEach((lockId) -> {
                    Lock.lockCreatedAcquireOpportunity(persistenceContext, lockId);
                });
            }
            case LOCK_RELEASED -> {
                request.locks().forEach((lockId) -> {
                    Lock.lockReleasedAcquireOpportunity(persistenceContext, lockId);
                });
            }
        }
    }
}
