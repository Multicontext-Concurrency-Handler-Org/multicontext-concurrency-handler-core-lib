package domain.event.impls;

import domain.entity.vos.events.HangingDeadlockVO;
import domain.enums.DomainEventType;
import domain.event.DomainEvent;

public class HangingDeadlock extends DomainEvent {
    public HangingDeadlock(HangingDeadlockVO content) {
        super(DomainEventType.HANGING_DEADLOCK, content);
    }
}
