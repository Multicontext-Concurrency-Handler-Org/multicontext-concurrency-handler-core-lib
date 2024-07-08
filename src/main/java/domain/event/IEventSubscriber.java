package domain.event;

import domain.enums.DomainEventType;

public interface IEventSubscriber {
    void notify(DomainEvent event);
    boolean isInterested(DomainEventType event);
}
