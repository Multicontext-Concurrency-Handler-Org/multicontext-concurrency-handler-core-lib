package lib.usecase.impls;

import domain.repository.PersistenceContext;
import lib.dto.ConsumeAcquireLockDTO;
import lib.exceptions.MCHConstraintViolation;
import lib.usecase.MCHUseCase;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class AcquireLockRequestUseCase extends MCHUseCase<ConsumeAcquireLockDTO> {
    private static final Logger logger = LogManager.getLogger();
    private final PersistenceContext persistenceContext;

    @Override
    protected void execute(ConsumeAcquireLockDTO acquireLockDTO) {

    }
}
