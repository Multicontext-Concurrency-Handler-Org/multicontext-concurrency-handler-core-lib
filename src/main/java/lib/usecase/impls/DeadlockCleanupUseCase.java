package lib.usecase.impls;

import domain.entity.Lock;
import domain.entity.vos.events.HangingDeadlockVO;
import domain.error.DomainErrorException;
import domain.error.UnexpectedErrorException;
import domain.event.EventPublisher;
import domain.event.impls.HangingDeadlock;
import domain.repository.PersistenceContext;
import domain.services.LockService;
import domain.services.ProcessService;
import lib.dto.ConsumeDeadlockCleanupEventDTO;
import lib.dto.ConsumeReleaseLockDTO;
import lib.usecase.MCHUseCase;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@AllArgsConstructor
public class DeadlockCleanupUseCase extends MCHUseCase<ConsumeDeadlockCleanupEventDTO> {
    public static final String PROCESS = "deadlock_cleanup";

    private final LockService lockService;
    private final ReleaseLockRequestUseCase releaseLockRequestUseCase;

    @Override
    protected void execute(ConsumeDeadlockCleanupEventDTO dto) {
        List<Lock> deadlocks = this.lockService.findExpiredLocks();

        deadlocks.parallelStream().forEach(lock -> {
            if (lock.getProcess().getIsManualDeadlockCleaningRequired()) {
                EventPublisher.publishEvent(new HangingDeadlock(new HangingDeadlockVO(lock.getId())));
            } else {
                releaseLockRequestUseCase.call(new ConsumeReleaseLockDTO(dto.version(), lock.getId(), PROCESS));
            }
        });
    }
}
