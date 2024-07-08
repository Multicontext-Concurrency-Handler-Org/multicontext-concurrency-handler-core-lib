package lib.usecase.impls;

import domain.repository.PersistenceContext;
import lib.dto.ConsumeAcquireLockDTO;
import lib.errors.MCHConstraintViolation;
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

    @Override
    protected List<MCHConstraintViolation> validate(ConsumeAcquireLockDTO dto) {
        return new ArrayList<>() {{
            if(Objects.isNull(dto)) {
                add(new MCHConstraintViolation("ConsumeAcquireLockDTO can't be null"));
            } else {
                if(Objects.isNull(dto.version())) {
                    add(new MCHConstraintViolation("ConsumeAcquireLockDTO.version can't be null"));
                } else if(dto.version() < 1) {
                    add(new MCHConstraintViolation("ConsumeAcquireLockDTO.version must be greater than zero"));
                }

                if(Objects.isNull(dto.process())) {
                    add(new MCHConstraintViolation("ConsumeAcquireLockDTO.process can't be null"));
                }
            }
        }};
    }
}
