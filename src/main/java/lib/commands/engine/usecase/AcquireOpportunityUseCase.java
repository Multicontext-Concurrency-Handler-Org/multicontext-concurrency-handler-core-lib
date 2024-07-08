package lib.commands.engine.usecase;

import domain.services.LockService;
import lib.commands.engine.dto.StateChangeEventDTO;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

@AllArgsConstructor
public class AcquireOpportunityUseCase {
    private static final Logger logger = LogManager.getLogger();
    private final LockService lockService;

    public void execute(StateChangeEventDTO request) {
        this.validate(request);

        switch (request.event()) {
            case LOCK_CREATED -> request.locks().forEach(this.lockService::lockCreatedAcquireOpportunity);
            case LOCK_RELEASED -> request.locks().forEach(this.lockService::lockReleasedAcquireOpportunity);
        }
        logger.info(String.format("AcquireOpportunityUseCase executed for %d locks", request.locks().size()));
    }

    private void validate(StateChangeEventDTO request) {
        Objects.requireNonNull(request, "StateChangeEventDTO request should never be null");
        Objects.requireNonNull(request.event(), "StateChangeEventDTO.event request should never be null");
        Objects.requireNonNull(request.locks(), "StateChangeEventDTO.locks request should never be null");
    }
}
