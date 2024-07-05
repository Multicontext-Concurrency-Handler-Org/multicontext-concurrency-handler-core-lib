package domain.event.impls;

import domain.entity.vos.events.DomainErrorEventVO;
import domain.enums.DomainEventType;
import domain.event.DomainEvent;

public class DomainErrorEvent extends DomainEvent {
    public DomainErrorEvent(DomainErrorEventVO content) {
        super(DomainEventType.DOMAIN_ERROR, content);
    }
}
