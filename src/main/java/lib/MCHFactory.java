package lib;

import domain.event.EventPublisher;
import domain.event.IEventSubscriber;
import domain.event.impls.DomainErrorEvent;
import domain.event.impls.LockAcquiredEvent;
import domain.event.impls.StateChangeEvent;
import domain.repository.ILockRepository;
import domain.repository.IProcessRepository;
import domain.repository.PersistenceContext;
import domain.services.LockService;
import domain.services.ProcessService;
import lib.subscribers.DefaultErrorSubscriber;
import lib.subscribers.DefaultLockAcquiredSubscriber;
import lib.subscribers.DefaultStateChangeSubscriber;
import lib.subscribers.producer.IProducer;
import lib.usecase.impls.AcquireLockRequestUseCase;
import lib.usecase.impls.AcquireOpportunityUseCase;
import lib.usecase.impls.DeadlockCleanupUseCase;
import lib.usecase.impls.ReleaseLockRequestUseCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class MCHFactory {
    private static final Logger logger = LogManager.getLogger();
    public static IMulticontextConcurrencyHandler create(
            IProcessRepository processRepository,
            ILockRepository lockRepository,
            IProducer<DomainErrorEvent> domainErrorEventProducer,
            IProducer<LockAcquiredEvent> lockAcquiredEventProducer,
            IProducer<StateChangeEvent> stateChangeEventProducer
    ) {
        Objects.requireNonNull(processRepository, "Required argument IProcessRepository processRepository");
        Objects.requireNonNull(lockRepository, "Required argument ILockRepository lockRepository");
        Objects.requireNonNull(domainErrorEventProducer, "Required argument IProducer<DomainErrorEvent> domainErrorEventProducer");
        Objects.requireNonNull(lockAcquiredEventProducer, "Required argument IProducer<LockAcquiredEvent> lockAcquiredEventProducer");
        Objects.requireNonNull(stateChangeEventProducer, "Required argument IProducer<StateChangeEvent> stateChangeEventProducer");

        var eventSubscribers = new ArrayList<IEventSubscriber>() {{
            add(new DefaultErrorSubscriber(domainErrorEventProducer));
            add(new DefaultLockAcquiredSubscriber(lockAcquiredEventProducer));
            add(new DefaultStateChangeSubscriber(stateChangeEventProducer));
        }};
        EventPublisher.registerSubscribers(eventSubscribers);

        var persistenceContext = new PersistenceContext(processRepository, lockRepository);

        var lockService = new LockService(persistenceContext);
        var processService = new ProcessService(persistenceContext);

        return new MCH(
            new AcquireOpportunityUseCase(lockService),
            new AcquireLockRequestUseCase(processService, lockService),
            new ReleaseLockRequestUseCase(processService, lockService),
            new DeadlockCleanupUseCase(processService, lockService)
        );
    }
}

