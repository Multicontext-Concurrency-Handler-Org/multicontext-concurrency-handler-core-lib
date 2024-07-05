package domain.event.impls;

import domain.entity.vos.events.AcquireLockEventVO;
import domain.enums.DomainEventType;
import domain.event.DomainEvent;


public class AcquireLockEvent extends DomainEvent {
    public AcquireLockEvent(AcquireLockEventVO content) {
        super(DomainEventType.ACQUIRE_LOCK, content);
    }
}
