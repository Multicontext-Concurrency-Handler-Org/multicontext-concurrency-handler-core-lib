package domain.entity.vos.process;

import domain.entity.Process;
import domain.enums.LockLevel;

public record ConcurrencyVO(
        Process process,
        LockLevel level
) { }
