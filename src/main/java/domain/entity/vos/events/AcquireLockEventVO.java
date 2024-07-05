package domain.entity.vos.events;

import java.util.List;

public record AcquireLockEventVO(
        Integer version,
        String process,
        String notificationTarget,
        List<String> workingSet,
        Object payload
) { }
