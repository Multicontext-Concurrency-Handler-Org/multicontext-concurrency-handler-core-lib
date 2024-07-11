package lib.subscribers.producer;

import domain.event.DomainEvent;

public interface IProducer {
    void produce(DomainEvent content);
}
