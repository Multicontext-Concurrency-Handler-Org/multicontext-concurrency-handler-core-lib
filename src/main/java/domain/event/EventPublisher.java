package domain.event;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class EventPublisher {
    private static final Logger logger = LogManager.getLogger();
    private static final List<IEventSubscriber> subscribers = new ArrayList<>();

    public static void registerSubscribers(List<IEventSubscriber> subscribers) {
        for(var subscriber: subscribers) {
            EventPublisher.subscribers.add(subscriber);
            logger.debug(String.format("Registered IEventSubscriber %s", subscriber.getClass().getName()));
        }
    }

    public static void publishEvent(DomainEvent event) {
        EventPublisher.subscribers.forEach(subscriber -> {
            if(subscriber.isInterested(event.getType())) {
                subscriber.notify(event);
                logger.debug(String.format("Notified IEventSubscriber %s", subscriber.getClass().getName()));
            }
        });
    }
}
