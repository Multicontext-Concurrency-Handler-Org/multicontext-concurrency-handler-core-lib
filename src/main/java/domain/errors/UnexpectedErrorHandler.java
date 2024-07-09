package domain.errors;

import domain.entity.vos.events.DomainErrorEventVO;
import domain.enums.DomainErrorType;
import domain.event.impls.DomainErrorEvent;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;

public class UnexpectedErrorHandler extends DomainErrorHandler {
    private final Exception unexpectedException;
    public UnexpectedErrorHandler(Exception e) {
        this.unexpectedException = e;
    }

    @Override
    protected DomainErrorEvent toEvent() {
        return new DomainErrorEvent(
                new DomainErrorEventVO(
                        DomainErrorType.UNEXPECTED_EXCEPTION,
                        this.getMessage(),
                        Instant.now()
                )
        );
    }

    private String getMessage() {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        this.unexpectedException.printStackTrace(pw);
        return this.unexpectedException.getMessage().concat("\n").concat(pw.toString());
    }
}
