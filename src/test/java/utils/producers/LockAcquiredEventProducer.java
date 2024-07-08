package utils.producers;

import domain.event.impls.LockAcquiredEvent;
import lib.subscribers.producer.IProducer;
import lombok.AllArgsConstructor;
import utils.IHandleEventLambda;

@AllArgsConstructor
public class LockAcquiredEventProducer implements IProducer<LockAcquiredEvent> {
    private final IHandleEventLambda<LockAcquiredEvent> handleEventLambda;

    @Override
    public void produce(LockAcquiredEvent content) {
        this.handleEventLambda.execute(content);
    }
}
