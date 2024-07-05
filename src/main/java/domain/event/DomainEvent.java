package domain.event;

import domain.enums.DomainEventType;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public abstract class DomainEvent {
    private final DomainEventType type;
    private final Object content;
}
