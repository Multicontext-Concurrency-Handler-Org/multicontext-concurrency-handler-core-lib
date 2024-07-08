package lib.dto;

import java.util.List;

public record ConsumeAcquireLockDTO(
        Integer version,
        String process,
        List<String> workingSet,
        Object context
) { }
