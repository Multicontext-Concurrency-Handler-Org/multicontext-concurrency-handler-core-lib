package lib.commands.user.usecase;

import domain.event.EventPublisher;
import domain.event.impls.ReleaseLockEvent;
import lib.commands.user.dto.SendReleaseLockEventDTO;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SendReleaseLockEventUseCase {
    public void execute(SendReleaseLockEventDTO request) {
        var vo = request.toValueObject();
        var event = new ReleaseLockEvent(vo);
        EventPublisher.publishEvent(event);
    }
}
