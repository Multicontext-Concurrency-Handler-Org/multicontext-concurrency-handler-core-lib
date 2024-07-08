package lib;

import domain.entity.vos.events.DomainErrorEventVO;
import domain.enums.DomainEventType;
import domain.event.EventPublisher;
import domain.event.impls.DomainErrorEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import utils.GenericEventSubscriberTestUtil;

import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
class MulticontextConcurrencyHandlerFactoryTest {
    @Nested
    class CreateMulticontextConcurrencyHandler {
        @Test
        @DisplayName("it should be able to create a multicontext concurrency handler instance")
        void create() {
            var originalEventContent = new DomainErrorEventVO();

            var subscriber = new GenericEventSubscriberTestUtil<>(
                    DomainEventType.DOMAIN_ERROR,
                    DomainErrorEventVO.class,
                    eventContent -> {
                        Assertions.assertEquals(originalEventContent, eventContent);
                    }
            );

            MulticontextConcurrencyHandlerFactory.create(
                    null,
                    null,
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