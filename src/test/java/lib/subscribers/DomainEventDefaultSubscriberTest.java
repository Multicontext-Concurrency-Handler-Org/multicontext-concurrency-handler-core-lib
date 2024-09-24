package lib.subscribers;

import domain.entity.vos.events.DomainErrorEventVO;
import domain.entity.vos.events.StateChangeEventVO;
import domain.enums.DomainErrorType;
import domain.enums.StateChange;
import domain.event.EventPublisher;
import domain.event.impls.DomainErrorEvent;
import domain.event.impls.StateChangeEvent;
import lib.subscribers.producer.IProducer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DomainEventDefaultSubscriberTest {
    @Mock
    IProducer mockProducer;

    @Test
    @DisplayName("Can be registered")
    public void register() {
        EventPublisher.registerSubscribers(List.of(new DomainEventDefaultSubscriber(mockProducer)));
    }

    @Test
    @DisplayName("All domain events get produced through the injected producer")
    public void produceEvents() {
        var defaultSubscriber = new DomainEventDefaultSubscriber(mockProducer);
        EventPublisher.registerSubscribers(List.of(defaultSubscriber));

        var domainError = new DomainErrorEvent(new DomainErrorEventVO(DomainErrorType.INVALID_STATE, "foo", Instant.now()));
        var acquiredLock = new StateChangeEvent(new StateChangeEventVO(1, StateChange.LOCK_CREATED, List.of("")));

        EventPublisher.publishEvent(domainError);
        EventPublisher.publishEvent(acquiredLock);

        verify(mockProducer, times(1)).produce(domainError);
        verify(mockProducer, times(1)).produce(acquiredLock);
    }
}
