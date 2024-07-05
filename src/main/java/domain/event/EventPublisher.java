package domain.event;

import java.util.ArrayList;
import java.util.List;

public class EventPublisher {
    private static final List<IEventSubscriber> subscribers = new ArrayList<>();

    public static void registerSubscribers(List<IEventSubscriber> subscribers) {
        EventPublisher.subscribers.addAll(subscribers);
    }

    public static void publishEvent(DomainEvent event) {
        EventPublisher.subscribers.forEach(subscriber -> {
            if(subscriber.isInterested(event.getType())) {
                subscriber.notify(event);
            }
        });
    }
}
