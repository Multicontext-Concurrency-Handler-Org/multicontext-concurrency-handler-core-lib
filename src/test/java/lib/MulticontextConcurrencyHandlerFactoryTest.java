package lib;

import domain.entity.vos.events.DomainErrorEventVO;
import domain.enums.DomainErrorType;
import domain.enums.DomainEventType;
import domain.event.DomainEvent;
import domain.event.EventPublisher;
import domain.event.IEventSubscriber;
import domain.event.impls.DomainErrorEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import utils.GenericEventSubscriberTestUtil;
import utils.InMemoryLockRepository;
import utils.InMemoryProcessRepository;

import java.time.Instant;
import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
class MulticontextConcurrencyHandlerFactoryTest {
    @Nested
    class CreateMulticontextConcurrencyHandler {
        @Test
        @DisplayName("MCHFactory deals with null entries")
        void nullEntriesCreate() {
            Assertions.assertThrowsExactly(NullPointerException.class, () -> {
                MulticontextConcurrencyHandlerFactory.create(
                        null,
                        null,
                        null
                );
            });

            var processRepository = new InMemoryProcessRepository();
            Assertions.assertThrowsExactly(NullPointerException.class, () -> {
                MulticontextConcurrencyHandlerFactory.create(
                        processRepository,
                        null,
                        null
                );
            });

            var lockRepository = new InMemoryLockRepository();
            Assertions.assertThrowsExactly(NullPointerException.class,() -> {
                MulticontextConcurrencyHandlerFactory.create(
                        processRepository,
                        lockRepository,
                        null
                );
            });

            var eventSubscribers = new ArrayList<IEventSubscriber>();
            Assertions.assertDoesNotThrow(() -> {
                MulticontextConcurrencyHandlerFactory.create(
                        processRepository,
                        lockRepository,
                        eventSubscribers
                );
            });

            eventSubscribers.add(new GenericEventSubscriberTestUtil<>(
                    DomainEventType.DOMAIN_ERROR,
                    DomainErrorEventVO.class,
                    null
            ));

            Assertions.assertDoesNotThrow(() -> {
                MulticontextConcurrencyHandlerFactory.create(
                        processRepository,
                        lockRepository,
                        eventSubscribers
                );
            });
        }

        @Test
        @DisplayName("MCHFactory register event subscribers")
        void example() {
            var originalEventContent = new DomainErrorEventVO(
                    DomainErrorType.INVALID_STATE,
                    "test domain error event",
                    Instant.now()
            );

            var subscriber = new GenericEventSubscriberTestUtil<>(
                    DomainEventType.DOMAIN_ERROR,
                    DomainErrorEventVO.class,
                    eventContent -> {
                        Assertions.assertEquals(originalEventContent, eventContent);
                    }
            );

            MulticontextConcurrencyHandlerFactory.create(
                    new InMemoryProcessRepository(),
                    new InMemoryLockRepository(),
                    new ArrayList<>() {
                        {
                            add(subscriber);
                        }
                    }
            );

            EventPublisher.publishEvent(new DomainErrorEvent(originalEventContent));
        }
    }
}