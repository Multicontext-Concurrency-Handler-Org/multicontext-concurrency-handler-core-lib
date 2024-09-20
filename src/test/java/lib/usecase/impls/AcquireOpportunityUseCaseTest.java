package lib.usecase.impls;

import domain.enums.DomainEventType;
import domain.enums.StateChange;
import domain.event.EventPublisher;
import domain.services.LockService;
import lib.dto.ConsumeStateChangeEventDTO;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import utils.EventSubscriberTestUtil;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AcquireOpportunityUseCaseTest {
    @Mock
    LockService lockService;

    @InjectMocks
    AcquireOpportunityUseCase acquireOpportunityUseCase;

    @Test
    @DisplayName("It should be able to construct AcquireOpportunityUseCase")
    void constructAcquireOpportunityUseCase() {
        Assertions.assertDoesNotThrow(() -> new AcquireOpportunityUseCase(lockService));
    }

    @Test
    @DisplayName("It should be able to execute for LOCK_CREATED without exceptions")
    @SneakyThrows
    void executeLockCreated() {
        var acquireOpportunityUseCase = new AcquireOpportunityUseCase(lockService);
        var dto = new ConsumeStateChangeEventDTO(1, StateChange.LOCK_CREATED, List.of("foo", "bar"));

        acquireOpportunityUseCase.execute(dto);

        verify(lockService, times(1)).lockCreatedAcquireOpportunity("foo");
        verify(lockService, times(1)).lockCreatedAcquireOpportunity("bar");
    }

    @Test
    @DisplayName("It should be able to execute for LOCK_RELEASED without exceptions")
    @SneakyThrows
    void executeLockReleased() {
        var acquireOpportunityUseCase = new AcquireOpportunityUseCase(lockService);
        var dto = new ConsumeStateChangeEventDTO(1, StateChange.LOCK_RELEASED, List.of("foo", "bar"));

        acquireOpportunityUseCase.execute(dto);

        verify(lockService, times(1)).lockReleasedAcquireOpportunity("foo");
        verify(lockService, times(1)).lockReleasedAcquireOpportunity("bar");
    }
}
