package lib.usecase.impls;

import domain.services.LockService;
import lib.dto.ConsumeStateChangeEventDTO;
import lib.errors.MCHConstraintViolation;
import lib.usecase.MCHUseCase;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class AcquireOpportunityUseCase extends MCHUseCase<ConsumeStateChangeEventDTO> {
    private static final Logger logger = LogManager.getLogger();
    private final LockService lockService;

    @Override
    protected void execute(ConsumeStateChangeEventDTO request) {
        this.validate(request);

        switch (request.event()) {
            case LOCK_CREATED -> request.locks().forEach(this.lockService::lockCreatedAcquireOpportunity);
            case LOCK_RELEASED -> request.locks().forEach(this.lockService::lockReleasedAcquireOpportunity);
        }
        logger.info(String.format("AcquireOpportunityUseCase executed for %d locks", request.locks().size()));
    }

    @Override
    protected List<MCHConstraintViolation> validate(ConsumeStateChangeEventDTO dto) {
        return new ArrayList<>() {{
            if(Objects.isNull(dto)) {
                add(new MCHConstraintViolation("ConsumeStateChangeEventDTO can't be null"));
            } else {
                if(Objects.isNull(dto.version())) {
                    add(new MCHConstraintViolation("ConsumeStateChangeEventDTO.version can't be null"));
                } else if(dto.version() < 1) {
                    add(new MCHConstraintViolation("ConsumeStateChangeEventDTO.version must be greater than zero"));
                }

                if(Objects.isNull(dto.event())) {
                    add(new MCHConstraintViolation("ConsumeStateChangeEventDTO.event can't be null"));
                }

                if(Objects.isNull(dto.locks())) {
                    add(new MCHConstraintViolation("ConsumeStateChangeEventDTO.locks can't be null"));
                }
            }
        }};
    }

}
