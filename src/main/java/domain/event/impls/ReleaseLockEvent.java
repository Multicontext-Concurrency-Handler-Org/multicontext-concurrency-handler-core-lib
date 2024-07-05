package domain.event.impls;

import domain.entity.vos.events.ReleaseLockEventVO;
import domain.enums.DomainEventType;
import domain.event.DomainEvent;

public class ReleaseLockEvent extends DomainEvent {
    public ReleaseLockEvent(ReleaseLockEventVO content) {
        super(DomainEventType.RELEASE_LOCK, content);
    }
}
