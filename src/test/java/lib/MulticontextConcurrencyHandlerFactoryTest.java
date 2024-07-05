package lib;

import domain.entity.vos.events.DomainErrorEventVO;
import domain.event.EventPublisher;
import domain.event.impls.DomainErrorEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import utils.TestDomainErrorEventSubscriber;

import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
class MulticontextConcurrencyHandlerFactoryTest {
    @Nested
    class CreateMulticontextConcurrencyHandler {
        @Test
        @DisplayName("it should be able to create a multicontext concurrency handler instance")
        void create() {
            Assertions.assertDoesNotThrow(() -> {
                MulticontextConcurrencyHandlerFactory.create(
                        null,
                        null,
                        new ArrayList<>() {{
                            add(new TestDomainErrorEventSubscriber());
                        }}
                );

                EventPublisher.publishEvent(new DomainErrorEvent(new DomainErrorEventVO()));
            });
        }
    }
}