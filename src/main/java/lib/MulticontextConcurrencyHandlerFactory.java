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

import java.util.List;

public final class MulticontextConcurrencyHandlerFactory {
    public static IMulticontextConcurrencyHandlerAPI create(
            IProcessRepository processRepository,
            ILockRepository lockRepository,
            List<IEventSubscriber> eventSubscribers
    ) {
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
