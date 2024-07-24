package domain.error;

import domain.event.EventPublisher;
import domain.event.impls.DomainErrorEvent;

public abstract class DomainErrorException extends Exception {
    protected abstract DomainErrorEvent toEvent();
    public void throwEvent() {
        var event = this.toEvent();
        EventPublisher.publishEvent(event);
    }
}
