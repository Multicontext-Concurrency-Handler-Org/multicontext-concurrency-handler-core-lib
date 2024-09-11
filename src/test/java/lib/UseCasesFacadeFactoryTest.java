package lib;

import cross.MCHWrongAbstractionUsage;
import domain.event.IEventSubscriber;
import domain.repository.ILockRepository;
import domain.repository.IProcessRepository;
import lib.configuration.PersistenceConfiguration;
import lib.configuration.SubscribersConfiguration;
import lib.subscribers.producer.IProducer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class UseCasesFacadeFactoryTest {
    IProcessRepository processRepositoryMock = mock(IProcessRepository.class);
    ILockRepository lockRepositoryMock = mock(ILockRepository.class);
    IProducer producerMock = mock(IProducer.class);
    IEventSubscriber eventSubscriberMock = mock(IEventSubscriber.class);

    @Nested
    class MCHCoreFactoryCreateNullChecks {
        @Test
        @DisplayName("Create MCH Instance with default subscribers")
        void createInstanceDefaultSubscribers() {
            Assertions.assertThrows(MCHWrongAbstractionUsage.class, () -> {
                MCHCoreFactory.create(
                        null,
                        producerMock
                );
            });

            Assertions.assertThrows(MCHWrongAbstractionUsage.class, () -> {
                MCHCoreFactory.create(
                        new PersistenceConfiguration(null, null),
                        producerMock
                );
            });

            Assertions.assertThrows(MCHWrongAbstractionUsage.class, () -> {
                MCHCoreFactory.create(
                        new PersistenceConfiguration(processRepositoryMock, null),
                        producerMock
                );
            });

            Assertions.assertThrows(MCHWrongAbstractionUsage.class, () -> {
                MCHCoreFactory.create(
                        new PersistenceConfiguration(null, lockRepositoryMock),
                        producerMock
                );
            });

            // valid
            Assertions.assertDoesNotThrow(() -> {
                MCHCoreFactory.create(
                        new PersistenceConfiguration(processRepositoryMock, lockRepositoryMock),
                        producerMock
                );
            });
        }

        @Test
        @DisplayName("Create MCH Instance with custom subscribers")
        void createInstanceCustomSubscribers() {
            Assertions.assertThrows(MCHWrongAbstractionUsage.class, () -> {
                MCHCoreFactory.create(
                        new PersistenceConfiguration(processRepositoryMock, lockRepositoryMock),
                        new SubscribersConfiguration(null, null, null)
                );
            });

            Assertions.assertThrows(MCHWrongAbstractionUsage.class, () -> {
                MCHCoreFactory.create(
                        new PersistenceConfiguration(processRepositoryMock, lockRepositoryMock),
                        new SubscribersConfiguration(true, null, null)
                );
            });

            Assertions.assertThrows(MCHWrongAbstractionUsage.class, () -> {
                MCHCoreFactory.create(
                        new PersistenceConfiguration(processRepositoryMock, lockRepositoryMock),
                        new SubscribersConfiguration(false, null, null)
                );
            });

            // valid
            Assertions.assertDoesNotThrow(() -> {
                MCHCoreFactory.create(
                        new PersistenceConfiguration(processRepositoryMock, lockRepositoryMock),
                        new SubscribersConfiguration(true, producerMock, null)
                );
            });

            // valid
            Assertions.assertDoesNotThrow(() -> {
                MCHCoreFactory.create(
                        new PersistenceConfiguration(processRepositoryMock, lockRepositoryMock),
                        new SubscribersConfiguration(true, producerMock, List.of(eventSubscriberMock))
                );
            });

            // valid
            Assertions.assertDoesNotThrow(() -> {
                MCHCoreFactory.create(
                        new PersistenceConfiguration(processRepositoryMock, lockRepositoryMock),
                        new SubscribersConfiguration(false, null, List.of(eventSubscriberMock))
                );
            });

            // only warn that producer won't be used (producer is only used to inject into default subscribers)
            Assertions.assertDoesNotThrow(() -> {
                MCHCoreFactory.create(
                        new PersistenceConfiguration(processRepositoryMock, lockRepositoryMock),
                        new SubscribersConfiguration(false, producerMock, List.of(eventSubscriberMock))
                );
            });
        }
    }
}