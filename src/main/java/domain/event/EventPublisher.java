package domain.event;

import domain.entity.vos.events.DomainErrorEventVO;
import domain.event.impls.DomainErrorEvent;
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
        if(event instanceof DomainErrorEvent domainErrorEvent
            && domainErrorEvent.getContent() instanceof DomainErrorEventVO errorEventVO) {
            logger.error("Publishing error event " + errorEventVO.message());
        }

        EventPublisher.subscribers.forEach(subscriber -> {
            if(subscriber.isInterested(event.getType())) {
                subscriber.notify(event);
                logger.debug(String.format("Notified IEventSubscriber %s", subscriber.getClass().getName()));
            }
        });
    }
}
