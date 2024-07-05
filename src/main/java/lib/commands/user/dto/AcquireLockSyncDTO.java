package lib.commands.user.dto;

import java.util.List;

public record AcquireLockSyncDTO(
        Integer version,
        String process,
        List<String> workingSet
) { }
