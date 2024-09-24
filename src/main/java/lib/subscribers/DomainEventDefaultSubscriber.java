package lib.subscribers;

import domain.enums.DomainEventType;
import domain.event.DomainEvent;
import domain.event.IEventSubscriber;
import lib.subscribers.producer.IProducer;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DomainEventDefaultSubscriber implements IEventSubscriber {
    private final IProducer domainEventProducer;

    @Override
    public void notify(DomainEvent event) {
        this.domainEventProducer.produce(event);
    }

    @Override
    public boolean isInterested(DomainEventType event) {
        return true;
    }
}
