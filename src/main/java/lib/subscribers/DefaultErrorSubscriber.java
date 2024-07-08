package lib.subscribers;

import domain.enums.DomainEventType;
import domain.event.DomainEvent;
import domain.event.IEventSubscriber;
import domain.event.impls.DomainErrorEvent;
import lib.subscribers.producer.IProducer;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DefaultErrorSubscriber implements IEventSubscriber {
    private final IProducer<DomainErrorEvent> producer;

    @Override
    public void notify(DomainEvent event) {
        if(event instanceof DomainErrorEvent domainErrorEvent) {
            this.producer.produce(domainErrorEvent);
        }
    }

    @Override
    public boolean isInterested(DomainEventType event) {
        return DomainEventType.DOMAIN_ERROR.equals(event);
    }
}
