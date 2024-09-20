package lib.usecase;

import domain.entity.vos.events.DomainErrorEventVO;
import domain.enums.DomainErrorType;
import domain.enums.DomainEventType;
import domain.error.DomainErrorException;
import domain.error.InvalidStateException;
import domain.error.UnexpectedErrorException;
import domain.event.DomainEvent;
import domain.event.EventPublisher;
import domain.event.IEventSubscriber;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jdk.jfr.Description;
import lib.exceptions.MCHConstraintViolationException;
import lombok.Getter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.EventSubscriberTestUtil;

import java.util.List;

record ThrowsExceptionUseCaseDTO(@NotNull @Positive Integer version) {}

class ThrowsExceptionUseCase extends MCHUseCase<ThrowsExceptionUseCaseDTO> {
    @Override
    protected void execute(ThrowsExceptionUseCaseDTO dto) throws DomainErrorException {
        throw new InvalidStateException("None actually");
    }
}

class ThrowsUnexpectedExceptionUseCase extends MCHUseCase<ThrowsExceptionUseCaseDTO> {
    @Override
    protected void execute(ThrowsExceptionUseCaseDTO dto) throws DomainErrorException {
        throw new RuntimeException("Surprise");
    }
}

public class MCHUseCaseTest {
    ThrowsExceptionUseCase throwsExceptionUseCase = new ThrowsExceptionUseCase();
    ThrowsUnexpectedExceptionUseCase throwsUnexpectedExceptionUseCase = new ThrowsUnexpectedExceptionUseCase();
    EventSubscriberTestUtil eventSubscriberTest;

    @BeforeEach
    void resetEventSubscriber() {
        this.eventSubscriberTest = new EventSubscriberTestUtil(List.of(DomainEventType.DOMAIN_ERROR));
        EventPublisher.registerSubscribers(List.of(eventSubscriberTest));
    }

    @Test
    @DisplayName("It should handle DomainErrorExceptions through call method")
    @Description("Domain errors should be handled as events")
    void callMethod() {
        Assertions.assertDoesNotThrow(() -> {
            throwsExceptionUseCase.call(new ThrowsExceptionUseCaseDTO(1));
            Assertions.assertEquals(1, eventSubscriberTest.getEvents().size());
        });
    }

    @Test
    @DisplayName("It should handle MCHConstraintViolationException through call method")
    @Description("Constraint violations should be handled as exceptions")
    void handleExceptionCallMethod() {
        Assertions.assertThrows(MCHConstraintViolationException.class, () -> {
            throwsExceptionUseCase.call(new ThrowsExceptionUseCaseDTO(0));
            Assertions.assertEquals(0, eventSubscriberTest.getEvents().size());
        });
    }

    @Test
    @DisplayName("It should handle UnexpectedExceptions through call method")
    void handleUnexpectedExceptionCallMethod() {
        Assertions.assertDoesNotThrow(() -> {
            throwsUnexpectedExceptionUseCase.call(new ThrowsExceptionUseCaseDTO(1));

            Assertions.assertEquals(1, eventSubscriberTest.getEvents().size());
            var vo = (DomainErrorEventVO) eventSubscriberTest.getEvents().get(0).getContent();

            Assertions.assertEquals(DomainEventType.DOMAIN_ERROR, eventSubscriberTest.getEvents().get(0).getType());
            Assertions.assertEquals(DomainErrorType.UNEXPECTED_EXCEPTION, vo.id());
        });
    }
}
