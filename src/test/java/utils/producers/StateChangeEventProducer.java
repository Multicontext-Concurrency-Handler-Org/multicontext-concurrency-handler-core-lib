package utils.producers;

import domain.event.impls.StateChangeEvent;
import lib.subscribers.producer.IProducer;
import lombok.AllArgsConstructor;
import utils.IHandleEventLambda;

@AllArgsConstructor
public class StateChangeEventProducer implements IProducer<StateChangeEvent> {
    private final IHandleEventLambda<StateChangeEvent> handleEventLambda;

    @Override
    public void produce(StateChangeEvent content) {
        this.handleEventLambda.execute(content);
    }
}
