package lib.subscribers;

import domain.enums.DomainEventType;
import domain.event.DomainEvent;
import domain.event.IEventSubscriber;
import domain.event.impls.LockAcquiredEvent;
import lib.subscribers.producer.IProducer;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DefaultLockAcquiredSubscriber implements IEventSubscriber {
    private final IProducer<LockAcquiredEvent> producer;
    @Override
    public void notify(DomainEvent event) {
        if(event instanceof LockAcquiredEvent lockAcquiredEvent) {
            this.producer.produce(lockAcquiredEvent);
        }
    }

    @Override
    public boolean isInterested(DomainEventType event) {
        return DomainEventType.LOCK_ACQUIRED.equals(event);
    }
}
