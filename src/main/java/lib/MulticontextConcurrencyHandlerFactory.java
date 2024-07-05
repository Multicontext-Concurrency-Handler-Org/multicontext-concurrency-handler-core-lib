package lib;

import domain.event.EventPublisher;
import domain.event.IEventSubscriber;
import domain.repository.ILockRepository;
import domain.repository.IProcessRepository;
import domain.repository.PersistenceContext;

import java.util.List;

public final class MulticontextConcurrencyHandlerFactory {
    public static IMulticontextConcurrencyHandlerAPI create(
            IProcessRepository processRepository,
            ILockRepository lockRepository,
            List<IEventSubscriber> eventSubscribers
    ) {
        EventPublisher.registerSubscribers(eventSubscribers);
        var persistenceContext = new PersistenceContext(processRepository, lockRepository);
        return new MulticontextConcurrencyHandler(persistenceContext);
    }
}
