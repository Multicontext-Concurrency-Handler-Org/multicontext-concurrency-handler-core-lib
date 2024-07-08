package domain.event.impls;

import domain.entity.vos.events.LockAcquiredEventVO;
import domain.enums.DomainEventType;
import domain.event.DomainEvent;

public class LockAcquiredEvent extends DomainEvent {

    public LockAcquiredEvent(LockAcquiredEventVO content) {
        super(DomainEventType.LOCK_ACQUIRED, content);
    }
}
