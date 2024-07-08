package lib;

import domain.event.EventPublisher;
import domain.event.IEventSubscriber;
import domain.repository.ILockRepository;
import domain.repository.IProcessRepository;
import domain.repository.PersistenceContext;
import domain.services.LockService;
import lib.usecase.impls.AcquireLockRequestUseCase;
import lib.usecase.impls.AcquireOpportunityUseCase;
import lib.usecase.impls.DeadlockCleanupUseCase;
import lib.usecase.impls.ReleaseLockRequestUseCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;

public final class MCHFactory {
    private static final Logger logger = LogManager.getLogger();
    public static IMulticontextConcurrencyHandler create(
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

        return new MCH(
            new AcquireOpportunityUseCase(lockService),
            new AcquireLockRequestUseCase(persistenceContext),
            new ReleaseLockRequestUseCase(persistenceContext),
            new DeadlockCleanupUseCase(persistenceContext)
        );
    }
}

