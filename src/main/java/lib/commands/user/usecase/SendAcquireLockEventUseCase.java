package lib.commands.user.usecase;

import domain.event.DomainEvent;
import domain.event.EventPublisher;
import domain.event.impls.AcquireLockEvent;
import domain.repository.PersistenceContext;
import lib.commands.user.dto.SendAcquireLockEventDTO;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SendAcquireLockEventUseCase {
    public void execute(SendAcquireLockEventDTO request) {
        var vo = request.toValueObject();
        var event = new AcquireLockEvent(vo);
        EventPublisher.publishEvent(event);
    }
}
