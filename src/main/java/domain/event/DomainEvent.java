package domain.event;

import domain.enums.DomainEventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class DomainEvent {
    private final DomainEventType type;
    private final Object content;
}
