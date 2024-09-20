package lib.usecase.impls;

import domain.entity.Lock;
import domain.entity.Process;
import domain.entity.vos.events.ReleaseLockEventVO;
import domain.enums.DomainEventType;
import domain.enums.LockReleaseMode;
import domain.event.EventPublisher;
import domain.services.LockService;
import domain.services.ProcessService;
import lib.dto.ConsumeAcquireLockDTO;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReleaseLockRequestUseCaseTest {
    @Mock
    ProcessService processService;
    @Mock
    LockService lockService;
    @InjectMocks
    ReleaseLockRequestUseCase releaseLockRequestUseCase;

    EventSubscriberTestUtil eventSubscriberTestUtil;

    @BeforeEach
    void setupEventSubscriber() {
        this.eventSubscriberTestUtil = new EventSubscriberTestUtil(List.of(DomainEventType.RELEASE_LOCK));
        EventPublisher.registerSubscribers(List.of(eventSubscriberTestUtil));
    }

    @Test
    @DisplayName("It should be able to construct ReleaseLockRequestUseCase")
    void constructReleaseLockRequestUseCase() {
        Assertions.assertDoesNotThrow(() -> new ReleaseLockRequestUseCase(processService, lockService));
    }

    @Test
    @DisplayName("It should be able to execute without exceptions")
    @SneakyThrows
    void executeReleaseModeEvent() {
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

        var lock = new Lock("uuid", 1, process, null, null);
        lock.startRunning();

        when(lockService.findLockByIdOrThrow(lock.getId())).thenReturn(lock);
        when(processService.findByNameOrThrow(process.getName())).thenReturn(process);

        var dto = new ConsumeReleaseLockDTO(1, lock.getId(), process.getName());
        releaseLockRequestUseCase.execute(dto);

        Assertions.assertEquals(1, eventSubscriberTestUtil.getEvents().size());
        ReleaseLockEventVO releaseLockEventVO = (ReleaseLockEventVO) eventSubscriberTestUtil.getEvents().get(0).getContent();
        Assertions.assertEquals(lock.getId(), releaseLockEventVO.lockId());
        Assertions.assertEquals(LockReleaseMode.EVENT, releaseLockEventVO.releaseMode());
    }

    @Test
    @DisplayName("It should be able to execute without exceptions")
    @SneakyThrows
    void executeReleaseModeAuto() {
        var process = new Process(
                "auto",
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

        var lock = new Lock("uuid", 1, process, null, null);
        lock.startRunning();

        when(lockService.findLockByIdOrThrow(lock.getId())).thenReturn(lock);

        var dto = new ConsumeReleaseLockDTO(1, lock.getId(), process.getName());
        releaseLockRequestUseCase.execute(dto);

        Assertions.assertEquals(1, eventSubscriberTestUtil.getEvents().size());
        ReleaseLockEventVO releaseLockEventVO = (ReleaseLockEventVO) eventSubscriberTestUtil.getEvents().get(0).getContent();
        Assertions.assertEquals(lock.getId(), releaseLockEventVO.lockId());
        Assertions.assertEquals(LockReleaseMode.AUTO, releaseLockEventVO.releaseMode());
    }

    @Test
    @DisplayName("It should be able to execute without exceptions")
    @SneakyThrows
    void executeReleaseModeManual() {
        var process = new Process(
                "manual",
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

        var lock = new Lock("uuid", 1, process, null, null);
        lock.startRunning();

        when(lockService.findLockByIdOrThrow(lock.getId())).thenReturn(lock);

        var dto = new ConsumeReleaseLockDTO(1, lock.getId(), process.getName());
        releaseLockRequestUseCase.execute(dto);

        Assertions.assertEquals(1, eventSubscriberTestUtil.getEvents().size());
        ReleaseLockEventVO releaseLockEventVO = (ReleaseLockEventVO) eventSubscriberTestUtil.getEvents().get(0).getContent();
        Assertions.assertEquals(lock.getId(), releaseLockEventVO.lockId());
        Assertions.assertEquals(LockReleaseMode.MANUAL, releaseLockEventVO.releaseMode());
    }
}
