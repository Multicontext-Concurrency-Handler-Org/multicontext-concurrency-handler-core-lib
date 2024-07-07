package utils;

import domain.entity.vos.events.*;
import domain.enums.DomainEventType;
import domain.event.DomainEvent;
import domain.event.IEventSubscriber;
import org.junit.jupiter.api.Assertions;

public class TestDomainErrorEventSubscriber implements IEventSubscriber  {
    @Override
    public void notify(DomainEvent event) {
        Assertions.assertEquals(DomainEventType.DOMAIN_ERROR, event.getType());
        Assertions.assertInstanceOf(DomainErrorEventVO.class, event.getContent());
    }

    @Override
    public boolean isInterested(DomainEventType event) {
        return DomainEventType.DOMAIN_ERROR.equals(event);
    }
}
