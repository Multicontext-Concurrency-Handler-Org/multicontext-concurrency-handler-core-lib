package lib.usecase.impls;

import domain.entity.Lock;
import domain.entity.vos.events.ReleaseLockEventVO;
import domain.enums.LockReleaseMode;
import domain.event.EventPublisher;
import domain.event.impls.ReleaseLockEvent;
import domain.error.DomainErrorException;
import domain.services.LockService;
import domain.services.ProcessService;
import lib.dto.ConsumeReleaseLockDTO;
import lib.usecase.MCHUseCase;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@AllArgsConstructor
public class ReleaseLockRequestUseCase extends MCHUseCase<ConsumeReleaseLockDTO> {
    private static final Logger logger = LogManager.getLogger();
    private final ProcessService processService;
    private final LockService lockService;

    @Override
    protected void execute(ConsumeReleaseLockDTO releaseLockDTO) throws DomainErrorException {
        Lock lock = lockService.findLockByIdOrThrow(releaseLockDTO.lockId());

        var releaseMode = LockReleaseMode.fromCode(releaseLockDTO.process()).orElse(LockReleaseMode.EVENT);
        if(LockReleaseMode.EVENT.equals(releaseMode)) {
            processService.findByNameOrThrow(releaseLockDTO.process());
        }

        var releasedLock = lock.releaseOrThrow(releaseMode);
        lockService.upsert(releasedLock);
        EventPublisher.publishEvent(
                new ReleaseLockEvent(
                        ReleaseLockEventVO.fromEntity(releasedLock)
                )
        );
    }
}
