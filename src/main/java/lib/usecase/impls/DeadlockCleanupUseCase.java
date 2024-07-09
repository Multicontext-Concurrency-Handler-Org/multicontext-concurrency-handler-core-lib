package lib.usecase.impls;

import domain.repository.PersistenceContext;
import domain.services.LockService;
import domain.services.ProcessService;
import lib.dto.ConsumeDeadlockCleanupEventDTO;
import lib.usecase.MCHUseCase;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@AllArgsConstructor
public class DeadlockCleanupUseCase extends MCHUseCase<ConsumeDeadlockCleanupEventDTO> {
    private static final Logger logger = LogManager.getLogger();
    private final ProcessService processService;
    private final LockService lockService;

    @Override
    protected void execute(ConsumeDeadlockCleanupEventDTO dto) {

    }
}
