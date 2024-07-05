package lib.commands.user.dto;

import domain.entity.vos.events.ReleaseLockEventVO;

public record SendReleaseLockEventDTO(
    String lockId,
    String process
) {
    public ReleaseLockEventVO toValueObject() {
        return new ReleaseLockEventVO(
                lockId,
                process
        );
    }
}
