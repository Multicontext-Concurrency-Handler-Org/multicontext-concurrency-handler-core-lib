package lib.subscribers;

import domain.enums.DomainEventType;
import domain.event.DomainEvent;
import domain.event.IEventSubscriber;
import domain.event.impls.StateChangeEvent;
import lib.subscribers.producer.IProducer;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DefaultStateChangeSubscriber implements IEventSubscriber {
    private final IProducer<StateChangeEvent> producer;

    @Override
    public void notify(DomainEvent event) {
        if(event instanceof StateChangeEvent stateChangeEvent) {
            this.producer.produce(stateChangeEvent);
        }
    }

    @Override
    public boolean isInterested(DomainEventType event) {
        return DomainEventType.STATE_CHANGE.equals(event);
    }
}
