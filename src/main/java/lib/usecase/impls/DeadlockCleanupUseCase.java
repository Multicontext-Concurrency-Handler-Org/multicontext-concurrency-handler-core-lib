package lib.usecase.impls;

import domain.repository.PersistenceContext;
import lib.dto.ConsumeDeadlockCleanupEventDTO;
import lib.exceptions.MCHConstraintViolation;
import lib.usecase.MCHUseCase;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@AllArgsConstructor
public class DeadlockCleanupUseCase extends MCHUseCase<ConsumeDeadlockCleanupEventDTO> {
    private static final Logger logger = LogManager.getLogger();
    private final PersistenceContext persistenceContext;

    @Override
    protected void execute(ConsumeDeadlockCleanupEventDTO dto) {

    }
}