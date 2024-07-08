package lib;

import domain.event.EventPublisher;
import domain.event.IEventSubscriber;
import domain.repository.ILockRepository;
import domain.repository.IProcessRepository;
import domain.repository.PersistenceContext;
import domain.services.LockService;
import lib.commands.engine.usecase.AcquireOpportunityUseCase;
import lib.commands.user.usecase.AcquireLockSyncUseCase;
import lib.commands.user.usecase.SendAcquireLockEventUseCase;
import lib.commands.user.usecase.SendReleaseLockEventUseCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class MulticontextConcurrencyHandlerFactory {
    private static final Logger logger = LogManager.getLogger();
    public static IMulticontextConcurrencyHandlerAPI create(
            IProcessRepository processRepository,
            ILockRepository lockRepository,
            List<IEventSubscriber> eventSubscribers
    ) {
        Objects.requireNonNull(processRepository, "Required argument IProcessRepository processRepository");
        Objects.requireNonNull(processRepository, "Required argument ILockRepository lockRepository");
        Objects.requireNonNull(processRepository, "Required argument List<IEventSubscriber> eventSubscribers");

        if(eventSubscribers.isEmpty()) {
            logger.warn("No event subscribers was provided, please notice that this only should be done for development or testing reasons");
        }

        EventPublisher.registerSubscribers(eventSubscribers);

        var persistenceContext = new PersistenceContext(processRepository, lockRepository);

        var lockService = new LockService(persistenceContext);

        return new MulticontextConcurrencyHandler(
                new SendAcquireLockEventUseCase(),
                new SendReleaseLockEventUseCase(),
                new AcquireLockSyncUseCase(persistenceContext),
                new AcquireOpportunityUseCase(lockService)
        );
    }
}

