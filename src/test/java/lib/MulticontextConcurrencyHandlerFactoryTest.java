package lib;

import domain.event.IEventSubscriber;
import domain.repository.ILockRepository;
import domain.repository.IProcessRepository;
import domain.repository.PersistenceContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

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
                        new ArrayList<>()
                );
            });
        }
    }
}