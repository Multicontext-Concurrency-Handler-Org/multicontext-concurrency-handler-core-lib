package domain.exceptions;

import domain.entity.vos.events.DomainErrorEventVO;
import domain.enums.DomainErrorType;
import domain.event.impls.DomainErrorEvent;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;

public class UnexpectedErrorException extends DomainErrorException {
    private final Exception unexpectedException;
    public UnexpectedErrorException(Exception e) {
        this.unexpectedException = e;
    }

    @Override
    protected DomainErrorEvent toEvent() {
        return new DomainErrorEvent(
                new DomainErrorEventVO(
                        DomainErrorType.UNEXPECTED_EXCEPTION,
                        String.format("Unexpected Exception: %s", this.buildMessage()),
                        Instant.now()
                )
        );
    }

    private String buildMessage() {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        this.unexpectedException.printStackTrace(pw);
        return this.unexpectedException.getMessage().concat("\n").concat(pw.toString());
    }
}
