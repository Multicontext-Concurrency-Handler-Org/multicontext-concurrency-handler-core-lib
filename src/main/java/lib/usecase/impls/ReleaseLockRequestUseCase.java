package lib.usecase.impls;

import domain.entity.Lock;
import domain.entity.vos.events.ReleaseLockEventVO;
import domain.enums.LockReleaseMode;
import domain.event.EventPublisher;
import domain.event.impls.ReleaseLockEvent;
import domain.repository.PersistenceContext;
import domain.services.LockService;
import domain.services.ProcessService;
import lib.dto.ConsumeReleaseLockDTO;
import lib.usecase.MCHUseCase;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
public class ReleaseLockRequestUseCase extends MCHUseCase<ConsumeReleaseLockDTO> {
    private static final Logger logger = LogManager.getLogger();
    private final ProcessService processService;
    private final LockService lockService;

    @Override
    protected void execute(ConsumeReleaseLockDTO releaseLockDTO) {
        Optional<Lock> lockOpt = lockService.findLockById(releaseLockDTO.lockId());
        if(lockOpt.isEmpty()) {
            logger.trace("Message will be skipped LockService.findLockById returned empty optional for lock " + releaseLockDTO.lockId());
            return;
        }

        var releaseModeOpt = LockReleaseMode.fromCode(releaseLockDTO.process());
        if(releaseModeOpt.isEmpty() || releaseModeOpt.get().equals(LockReleaseMode.EVENT)) {
            var processOpt = processService.findByName(releaseLockDTO.process());
            if(processOpt.isEmpty()) {
                logger.error("Message will be skipped ProcessService.findByName returned empty optional for process " + releaseLockDTO.process());
                return;
            }

            releaseModeOpt = Optional.of(LockReleaseMode.EVENT);
        }

        var releaseMode = releaseModeOpt.get();
        var lock = lockOpt.get();

        var releasedLockOpt = lock.release(releaseMode);
        if(releasedLockOpt.isEmpty()) {
            logger.warn("Message will be skipped Lock.release couldn't release lock " + releaseLockDTO.lockId());
            return;
        }

        var releasedLock = releasedLockOpt.get();
        lockService.upsert(releasedLock);
        EventPublisher.publishEvent(
                new ReleaseLockEvent(
                        ReleaseLockEventVO.fromEntity(releasedLock)
                )
        );
    }
}
