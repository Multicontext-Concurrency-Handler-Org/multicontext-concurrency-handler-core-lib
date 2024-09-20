package lib.usecase.impls;

import domain.entity.Lock;
import domain.entity.Process;
import domain.enums.DomainEventType;
import domain.event.EventPublisher;
import domain.services.LockService;
import lib.dto.ConsumeDeadlockCleanupEventDTO;
import lib.dto.ConsumeReleaseLockDTO;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import utils.EventSubscriberTestUtil;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeadlockCleanupUseCaseTest {
    @Mock
    LockService lockService;
    @Mock
    ReleaseLockRequestUseCase releaseLockRequestUseCase;
    @InjectMocks
    DeadlockCleanupUseCase deadlockCleanupUseCase;

    EventSubscriberTestUtil eventSubscriberTestUtil;

    @BeforeEach
    void setupEventSubscriber() {
        this.eventSubscriberTestUtil = new EventSubscriberTestUtil(List.of(DomainEventType.HANGING_DEADLOCK));
        EventPublisher.registerSubscribers(List.of(eventSubscriberTestUtil));
    }

    @Test
    @DisplayName("It should be able to construct DeadlockCleanupUseCase")
    void constructDeadlockCleanupUseCase() {
        Assertions.assertDoesNotThrow(() -> new DeadlockCleanupUseCase(lockService, releaseLockRequestUseCase));
    }


    @Test
    @DisplayName("It should be able to execute for isManualDeadlockCleaningRequired without exceptions")
    @SneakyThrows
    void executeIsManualDeadlockCleaningRequired() {
        var process = new Process(
                "manual_maintenance",
                1,
                Integer.MAX_VALUE,
                Duration.ofSeconds(30),
                true,
                true,
                null,
                Instant.now().minus(Duration.ofSeconds(60)),
                Instant.now().minus(Duration.ofSeconds(30)),
                List.of()
        );

        var lock = new Lock("foo", 1, process, null, null);
        when(lockService.findExpiredLocks()).thenReturn(List.of(lock));
        var dto = new ConsumeDeadlockCleanupEventDTO(1, Instant.now().minus(Duration.ofHours(1)));
        deadlockCleanupUseCase.execute(dto);
        Assertions.assertEquals(1, eventSubscriberTestUtil.getEvents().size());
    }

    @Test
    @DisplayName("It should be able to execute without exceptions")
    @SneakyThrows
    void executeIsNotManualDeadlockCleaningRequired() {
        var process = new Process(
                "cache_cleanup",
                1,
                Integer.MIN_VALUE,
                Duration.ofHours(1),
                false,
                false,
                null,
                Instant.now().minus(Duration.ofHours(2)),
                Instant.now().minus(Duration.ofHours(1)),
                List.of()
        );

        var lock = new Lock("foo", 1, process, null, null);
        when(lockService.findExpiredLocks()).thenReturn(List.of(lock));

        var dto = new ConsumeDeadlockCleanupEventDTO(1, Instant.now().minus(Duration.ofHours(1)));

        deadlockCleanupUseCase.execute(dto);

        var externalCallDto = new ConsumeReleaseLockDTO(lock.getVersion(), lock.getId(), "deadlock_cleanup");
        verify(releaseLockRequestUseCase, times(1)).call(externalCallDto);
        Assertions.assertEquals(0, eventSubscriberTestUtil.getEvents().size());
    }

    @Test
    @DisplayName("It should handle empty list of locks without exceptions")
    @SneakyThrows
    void executeWithEmptyLockList() {
        when(lockService.findExpiredLocks()).thenReturn(List.of());

        var dto = new ConsumeDeadlockCleanupEventDTO(1, Instant.now().minus(Duration.ofHours(1)));

        deadlockCleanupUseCase.execute(dto);

        // Verify no external call was made
        verify(releaseLockRequestUseCase, times(0)).call(any());
        Assertions.assertEquals(0, eventSubscriberTestUtil.getEvents().size());
    }
}
