package lib;

import domain.event.EventPublisher;
import domain.services.LockService;
import domain.services.ProcessService;
import lib.configuration.PersistenceConfiguration;
import lib.configuration.SubscribersConfiguration;
import lib.subscribers.DomainEventDefaultSubscriber;
import lib.subscribers.producer.IProducer;
import lib.usecase.UseCasesFacade;
import lib.usecase.impls.AcquireLockRequestUseCase;
import lib.usecase.impls.AcquireOpportunityUseCase;
import lib.usecase.impls.DeadlockCleanupUseCase;
import lib.usecase.impls.ReleaseLockRequestUseCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;

import static cross.MCHWrongAbstractionUsage.*;

public final class MCHCoreFactory {
    private static final Logger logger = LogManager.getLogger();
    /**
     * Default subscriber configuration usage
     * @param persistenceConfiguration repositories concrete implementation
     * @param producer producer that must handle all domain events
     * @return UseCasesFacade instance that implements IMCHCoreAPI
     */
    public static IMCHCore create(
            PersistenceConfiguration persistenceConfiguration,
            IProducer producer
    ) {
        assertNonNull(persistenceConfiguration, "persistenceConfiguration must not be null");
        assertNonNull(persistenceConfiguration.lockRepository(), "persistenceConfiguration.lockRepository() must not be null");
        assertNonNull(persistenceConfiguration.processRepository(), "persistenceConfiguration.processRepository() must not be null");
        assertNonNull(producer, "producer must not be null");

        return getUseCasesFacade(persistenceConfiguration, SubscribersConfiguration.getDefault(producer));
    }

    /**
     * Custom subscriber configuration usage
     * @param persistenceConfiguration repositories concrete implementation
     * @param subscribersConfiguration subscribers configuration
     * @return UseCasesFacade instance that implements IMCHCoreAPI
     */
    public static IMCHCore create(
            PersistenceConfiguration persistenceConfiguration,
            SubscribersConfiguration subscribersConfiguration
    ) {
        assertNonNull(persistenceConfiguration, "persistenceConfiguration must not be null");
        assertNonNull(persistenceConfiguration, "persistenceConfiguration must not be null");
        assertNonNull(persistenceConfiguration.lockRepository(), "persistenceConfiguration.lockRepository() must not be null");
        assertNonNull(persistenceConfiguration.processRepository(), "persistenceConfiguration.processRepository() must not be null");
        assertNonNull(subscribersConfiguration, "subscribersConfiguration must not be null");
        assertNonNull(subscribersConfiguration.useDefaultSubscriber(), "subscribersConfiguration.useDefaultSubscriber() must not be null");

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

        var releaseLockRequestUseCase = new ReleaseLockRequestUseCase(processService, lockService);
        return new UseCasesFacade(
                new AcquireOpportunityUseCase(lockService),
                new AcquireLockRequestUseCase(processService, lockService),
                releaseLockRequestUseCase,
                new DeadlockCleanupUseCase(lockService, releaseLockRequestUseCase)
        );
    }

    private static void registerSubscribers(SubscribersConfiguration subscribersConfiguration) {
        if(Boolean.TRUE.equals(subscribersConfiguration.useDefaultSubscriber())) {
            assertNonNull(
                    subscribersConfiguration.producer(),
                    "If SubscribersConfiguration.useDefaultSubscriber equals true, SubscribersConfiguration.producer must not be null"
            );

            EventPublisher.registerSubscribers(List.of(new DomainEventDefaultSubscriber(subscribersConfiguration.producer())));
        } else {
            if(Objects.nonNull(subscribersConfiguration.producer())) {
                logger.warn("SubscribersConfiguration.producer won't be used since SubscribersConfiguration.useDefaultSubscriber is false");
            }

            if(Objects.isNull(subscribersConfiguration.customSubscribers()) || subscribersConfiguration.customSubscribers().isEmpty()) {
                assertNonNull(
                        subscribersConfiguration.producer(),
                        "If SubscribersConfiguration.useDefaultSubscriber equals false, SubscribersConfiguration.customSubscribers must not be null or empty"
                );
            }
        }

        if(Objects.nonNull(subscribersConfiguration.customSubscribers())) {
            EventPublisher.registerSubscribers(subscribersConfiguration.customSubscribers());
        }
    }
}

