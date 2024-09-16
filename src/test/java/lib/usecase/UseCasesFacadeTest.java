package lib.usecase;

import lib.dto.ConsumeAcquireLockDTO;
import lib.dto.ConsumeDeadlockCleanupEventDTO;
import lib.dto.ConsumeReleaseLockDTO;
import lib.dto.ConsumeStateChangeEventDTO;
import lib.usecase.impls.AcquireLockRequestUseCase;
import lib.usecase.impls.AcquireOpportunityUseCase;
import lib.usecase.impls.DeadlockCleanupUseCase;
import lib.usecase.impls.ReleaseLockRequestUseCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class UseCasesFacadeTest {
    @Mock
    AcquireOpportunityUseCase acquireOpportunityUseCase;
    @Mock
    AcquireLockRequestUseCase acquireLockRequestUseCase;
    @Mock
    ReleaseLockRequestUseCase releaseLockRequestUseCase;
    @Mock
    DeadlockCleanupUseCase deadlockCleanupUseCase;
    @InjectMocks
    UseCasesFacade useCasesFacade;

    @Test
    @DisplayName("Construct Use Cases Facade")
    void constructUseCasesFacade() {
        Assertions.assertDoesNotThrow(() -> {
            new UseCasesFacade(
                    acquireOpportunityUseCase,
                    acquireLockRequestUseCase,
                    releaseLockRequestUseCase,
                    deadlockCleanupUseCase
            );
        });
    }

    @Test
    @DisplayName("Execute Acquire Opportunity Use Case")
    void executeAcquireOpportunityUseCase() {
        var dto = new ConsumeStateChangeEventDTO(null, null, null);
        useCasesFacade.consumeStateChange(dto);
        verify(acquireOpportunityUseCase, times(1)).call(dto);
    }

    @Test
    @DisplayName("Execute Acquire Lock Request Use Case")
    void executeAcquireLockRequestUseCase() {
        var dto = new ConsumeAcquireLockDTO(null, null, null, null);
        useCasesFacade.consumeAcquireLock(dto);
        verify(acquireLockRequestUseCase, times(1)).call(dto);
    }

    @Test
    @DisplayName("Execute Release Lock Request Use Case")
    void executeReleaseLockRequestUseCase() {
        var dto = new ConsumeReleaseLockDTO(null, null, null);
        useCasesFacade.consumeReleaseLock(dto);
        verify(releaseLockRequestUseCase, times(1)).call(dto);
    }

    @Test
    @DisplayName("Execute Deadlock Cleanup Use Case")
    void executeDeadlockCleanupUseCase() {
        var dto = new ConsumeDeadlockCleanupEventDTO(null, null);
        useCasesFacade.consumeDeadlockCleanupEvent(dto);
        verify(deadlockCleanupUseCase, times(1)).call(dto);
    }
}