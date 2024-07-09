package domain.services;

import domain.entity.Process;
import domain.entity.vos.events.DomainErrorEventVO;
import domain.enums.DomainErrorType;
import domain.enums.DomainEventType;
import domain.event.EventPublisher;
import domain.event.impls.DomainErrorEvent;
import domain.repository.PersistenceContext;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.util.Optional;

@AllArgsConstructor
public class ProcessService {
    private static final Logger logger = LogManager.getLogger();

    private final PersistenceContext persistenceContext;

    public Optional<Process> findByName(String process) {
        var processOpt = persistenceContext.processRepository().findByName(process);

        if(processOpt.isEmpty()) {
            EventPublisher.publishEvent(
                    new DomainErrorEvent(
                            new DomainErrorEventVO(
                                    DomainErrorType.MISSING_CONFIG,
                                    String.format("process %s not found by name", process),
                                    Instant.now()
                            )
                    )
            );
        }

        return processOpt;
    }
}
