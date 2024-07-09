package domain.errors;

import domain.event.EventPublisher;
import domain.event.impls.DomainErrorEvent;

public abstract class DomainErrorHandler {
    protected abstract DomainErrorEvent toEvent();
    public void throwEvent() {
        var event = this.toEvent();
        EventPublisher.publishEvent(event);
    }
}
