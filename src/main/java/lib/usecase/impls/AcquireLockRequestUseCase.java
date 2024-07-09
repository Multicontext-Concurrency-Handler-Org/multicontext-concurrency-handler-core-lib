package lib.usecase.impls;

import domain.entity.Lock;
import domain.entity.vos.events.AcquireLockEventVO;
import domain.event.EventPublisher;
import domain.event.impls.AcquireLockEvent;
import domain.repository.PersistenceContext;
import domain.services.LockService;
import domain.services.ProcessService;
import lib.dto.ConsumeAcquireLockDTO;
import lib.usecase.MCHUseCase;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@AllArgsConstructor
public class AcquireLockRequestUseCase extends MCHUseCase<ConsumeAcquireLockDTO> {
    private static final Logger logger = LogManager.getLogger();
    private final ProcessService processService;
    private final LockService lockService;

    @Override
    protected void execute(ConsumeAcquireLockDTO acquireLockDTO) {
        var processOpt = processService.findByName(acquireLockDTO.process());
        if(processOpt.isEmpty()) {
            logger.trace("Message will be skipped ProcessService.findByName returned empty optional for process " + acquireLockDTO.process());
            return;
        }

        var lock = new Lock(
                lockService.generateLockId(),
                acquireLockDTO.version(),
                processOpt.get(),
                acquireLockDTO.workingSet(),
                acquireLockDTO.context()
        );

        lockService.upsert(lock);
        EventPublisher.publishEvent(
                new AcquireLockEvent(
                        AcquireLockEventVO.fromEntity(lock)
                )
        );
    }
}
