package lib.commands.user.dto;

import domain.entity.vos.events.AcquireLockEventVO;

import java.util.List;

public record SendAcquireLockEventDTO(
    Integer version,
    String process,
    String notificationTarget,
    List<String> workingSet,
    Object payload
) {
    public AcquireLockEventVO toValueObject() {
        return new AcquireLockEventVO(
                version,
                process,
                notificationTarget,
                workingSet,
                payload
        );
    }
}
