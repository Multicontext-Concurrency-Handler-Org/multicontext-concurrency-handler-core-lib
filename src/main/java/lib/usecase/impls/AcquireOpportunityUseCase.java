package lib.usecase.impls;

import domain.services.LockService;
import lib.dto.ConsumeStateChangeEventDTO;
import lib.usecase.MCHUseCase;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
}
