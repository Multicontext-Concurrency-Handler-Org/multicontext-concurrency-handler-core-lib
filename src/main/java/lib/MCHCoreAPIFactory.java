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
import lib.configuration.PersistenceConfiguration;
import lib.configuration.SubscribersConfiguration;
import lib.subscribers.DomainEventProducerSubscriber;
import lib.subscribers.producer.IProducer;
import lib.usecase.UseCasesFacade;
import lib.usecase.impls.AcquireLockRequestUseCase;
import lib.usecase.impls.AcquireOpportunityUseCase;
import lib.usecase.impls.DeadlockCleanupUseCase;
import lib.usecase.impls.ReleaseLockRequestUseCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class MCHCoreAPIFactory {
    private static final Logger logger = LogManager.getLogger();
    /**
     * Default subscriber configuration usage
     * @param persistenceConfiguration repositories concrete implementation
     * @param producer producer that must handle all domain events
     * @return UseCasesFacade instance that implements IMCHCoreAPI
     */
    public static IMCHCoreAPI create(
            PersistenceConfiguration persistenceConfiguration,
            IProducer producer
    ) {
        Objects.requireNonNull(persistenceConfiguration, "Required argument PersistenceConfiguration persistenceConfiguration");
        Objects.requireNonNull(persistenceConfiguration.lockRepository(), "");
        Objects.requireNonNull(persistenceConfiguration.processRepository(), "");

        Objects.requireNonNull(producer, "Required argument IProducer producer");

        return getUseCasesFacade(persistenceConfiguration, SubscribersConfiguration.getDefault(producer));
    }

    /**
     * Custom subscriber configuration usage
     * @param persistenceConfiguration repositories concrete implementation
     * @param subscribersConfiguration subscribers configuration
     * @return UseCasesFacade instance that implements IMCHCoreAPI
     */
    public static IMCHCoreAPI create(
            PersistenceConfiguration persistenceConfiguration,
            SubscribersConfiguration subscribersConfiguration
    ) {
        Objects.requireNonNull(persistenceConfiguration, "Required argument PersistenceConfiguration persistenceConfiguration");
        Objects.requireNonNull(persistenceConfiguration.lockRepository(), "");
        Objects.requireNonNull(persistenceConfiguration.processRepository(), "");
        Objects.requireNonNull(subscribersConfiguration, "Required argument SubscribersConfiguration subscribersConfiguration");

        return getUseCasesFacade(persistenceConfiguration, subscribersConfiguration);
    }

    private static UseCasesFacade getUseCasesFacade(
            PersistenceConfiguration persistenceConfiguration,
            SubscribersConfiguration subscribersConfiguration
    ) {
        registerSubscribers(subscribersConfiguration);

        var persistenceContext = persistenceConfiguration.toPersistenceContext();

        var lockService = new LockService(persistenceContext);
        var processService = new ProcessService(persistenceContext);

        return new UseCasesFacade(
                new AcquireOpportunityUseCase(lockService),
                new AcquireLockRequestUseCase(processService, lockService),
                new ReleaseLockRequestUseCase(processService, lockService),
                new DeadlockCleanupUseCase(processService, lockService)
        );
    }

    private static void registerSubscribers(SubscribersConfiguration subscribersConfiguration) {
        if(Boolean.TRUE.equals(subscribersConfiguration.useDefaultSubscriber())) {
            Objects.requireNonNull(
                    subscribersConfiguration.producer(),
                    "If SubscribersConfiguration.useDefaultSubscriber equals true, SubscribersConfiguration.producer must not be null"
            );

            EventPublisher.registerSubscribers(List.of(new DomainEventProducerSubscriber(subscribersConfiguration.producer())));
        }

        if(Objects.nonNull(subscribersConfiguration.customSubscribers())) {
            EventPublisher.registerSubscribers(subscribersConfiguration.customSubscribers());
        }
    }
}

