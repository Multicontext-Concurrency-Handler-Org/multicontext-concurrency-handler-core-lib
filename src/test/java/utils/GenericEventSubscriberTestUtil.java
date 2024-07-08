package utils;

import domain.enums.DomainEventType;
import domain.event.DomainEvent;
import domain.event.IEventSubscriber;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Assertions;

@AllArgsConstructor
public class GenericEventSubscriberTestUtil<T> implements IEventSubscriber  {
    private final DomainEventType eventType;
    private final  Class<T> contentClass;
    private final IHandleEventNotifyLambda<T> handler;

    @Override
    public void notify(DomainEvent event) {
        Assertions.assertEquals(this.eventType, event.getType());
        Assertions.assertEquals(this.contentClass.getName(), event.getContent().getClass().getName());
        handler.execute(this.contentClass.cast(event.getContent()));
    }

    @Override
    public boolean isInterested(DomainEventType eventType) {
        return this.eventType.equals(eventType);
    }
}
