package lib.usecase.impls;

import domain.repository.PersistenceContext;
import lib.dto.ConsumeReleaseLockDTO;
import lib.exceptions.MCHConstraintViolation;
import lib.usecase.MCHUseCase;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@AllArgsConstructor
public class ReleaseLockRequestUseCase extends MCHUseCase<ConsumeReleaseLockDTO> {
    private static final Logger logger = LogManager.getLogger();
    private final PersistenceContext persistenceContext;

    @Override
    protected void execute(ConsumeReleaseLockDTO dto) {

    }
}