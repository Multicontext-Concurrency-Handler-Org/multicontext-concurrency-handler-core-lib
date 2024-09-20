package utils;

import domain.enums.DomainEventType;
import domain.event.DomainEvent;
import domain.event.IEventSubscriber;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class EventSubscriberTestUtil implements IEventSubscriber {
    private final List<DomainEvent> events = new ArrayList<>();
    private List<DomainEventType> eventTypes;

    public EventSubscriberTestUtil(List<DomainEventType> eventTypes) {
        if(Objects.nonNull(eventTypes) && eventTypes.size() > 0) {
            this.eventTypes = eventTypes;
        }
    }

    @Override
    public void notify(DomainEvent event) {
        events.add(event);
    }

    @Override
    public boolean isInterested(DomainEventType event) {
        return Objects.isNull(event) || this.eventTypes.contains(event);
    }
}