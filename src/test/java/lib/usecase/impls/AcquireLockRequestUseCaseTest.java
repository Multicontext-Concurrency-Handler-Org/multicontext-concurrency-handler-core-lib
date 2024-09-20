package lib.usecase.impls;

import domain.enums.DomainEventType;
import domain.error.DomainErrorException;
import domain.event.EventPublisher;
import domain.services.LockService;
import domain.services.ProcessService;
import lib.dto.ConsumeAcquireLockDTO;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import domain.entity.Process;
import utils.EventSubscriberTestUtil;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AcquireLockRequestUseCaseTest {
    @Mock
    ProcessService processService;
    @Mock
    LockService lockService;
    @Mock
    Process process;
    @InjectMocks
    AcquireLockRequestUseCase acquireLockRequestUseCase;

    EventSubscriberTestUtil eventSubscriberTestUtil;

    @BeforeEach
    void setupEventSubscriber() {
        this.eventSubscriberTestUtil = new EventSubscriberTestUtil(List.of(DomainEventType.ACQUIRE_LOCK));
        EventPublisher.registerSubscribers(List.of(eventSubscriberTestUtil));
    }

    @Test
    @DisplayName("It should be able to construct AcquireLockRequestUseCase")
    void constructAcquireLockRequestUseCase() {
        Assertions.assertDoesNotThrow(() -> new AcquireLockRequestUseCase(processService, lockService));
    }


    @Test
    @DisplayName("It should be able to execute without exceptions")
    @SneakyThrows
    void execute() {
        when(processService.findByNameOrThrow(anyString())).thenReturn(process);
        when(lockService.generateLockId()).thenReturn("uuid");
        var dto = new ConsumeAcquireLockDTO(1, "foo", null, null);

        Assertions.assertDoesNotThrow(() -> {
            acquireLockRequestUseCase.execute(dto);
            Assertions.assertEquals(1, eventSubscriberTestUtil.getEvents().size());
        });
    }
}
