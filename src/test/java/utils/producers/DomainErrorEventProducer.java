package utils.producers;

import domain.event.impls.DomainErrorEvent;
import lib.subscribers.producer.IProducer;
import lombok.AllArgsConstructor;
import utils.IHandleEventLambda;

@AllArgsConstructor
public class DomainErrorEventProducer implements IProducer<DomainErrorEvent> {
    private final IHandleEventLambda<DomainErrorEvent> handleEventLambda;
    @Override
    public void produce(DomainErrorEvent content) {
        this.handleEventLambda.execute(content);
    }
}
