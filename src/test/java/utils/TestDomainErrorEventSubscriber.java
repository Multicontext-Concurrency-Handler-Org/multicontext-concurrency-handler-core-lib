package utils;

import domain.entity.vos.events.*;
import domain.enums.DomainEventType;
import domain.event.DomainEvent;
import domain.event.IEventSubscriber;

public class TestDomainErrorEventSubscriber implements IEventSubscriber  {
    @Override
    public void notify(DomainEvent event) {
        if(!(event.getContent() instanceof DomainErrorEventVO)) {
            throw new RuntimeException("invalid event content");
        }
    }

    @Override
    public boolean isInterested(DomainEventType event) {
        return DomainEventType.DOMAIN_ERROR.equals(event);
    }
}
