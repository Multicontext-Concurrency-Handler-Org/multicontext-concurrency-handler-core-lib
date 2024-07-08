package lib;

import domain.event.impls.DomainErrorEvent;
import domain.event.impls.LockAcquiredEvent;
import domain.event.impls.StateChangeEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import utils.producers.DomainErrorEventProducer;
import utils.producers.LockAcquiredEventProducer;
import utils.producers.StateChangeEventProducer;
import utils.repositories.InMemoryLockRepository;
import utils.repositories.InMemoryProcessRepository;

@ExtendWith(MockitoExtension.class)
class MCHFactoryTest {
    @Nested
    class CreateMCH {
        @Test
        @DisplayName("Create MCH Instance with default subscribers")
        void createInstanceDefaultSubscribers() {
            Assertions.assertDoesNotThrow(() -> {
                MCHFactory.create(
                        new InMemoryProcessRepository(),
                        new InMemoryLockRepository(),
                        new DomainErrorEventProducer((DomainErrorEvent e) -> {}),
                        new LockAcquiredEventProducer((LockAcquiredEvent e) -> {}),
                        new StateChangeEventProducer((StateChangeEvent e) -> {})
                );
            });
        }
    }
}