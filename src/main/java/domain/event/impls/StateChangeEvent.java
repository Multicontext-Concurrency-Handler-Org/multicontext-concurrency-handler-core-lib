package domain.event.impls;

import domain.entity.vos.events.StateChangeEventVO;
import domain.enums.DomainEventType;
import domain.event.DomainEvent;

public class StateChangeEvent extends DomainEvent {
    public StateChangeEvent(StateChangeEventVO content) {
        super(DomainEventType.STATE_CHANGE, content);
    }
}
