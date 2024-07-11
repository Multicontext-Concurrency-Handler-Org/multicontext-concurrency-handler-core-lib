package lib.configuration;

import domain.event.IEventSubscriber;
import lib.subscribers.producer.IProducer;

import java.util.ArrayList;
import java.util.List;

public record SubscribersConfiguration(Boolean useDefaultSubscriber, IProducer producer, List<IEventSubscriber> customSubscribers) {
    public static SubscribersConfiguration getDefault(IProducer producer) {
        return  new SubscribersConfiguration(true, producer, new ArrayList<>());
    }
}