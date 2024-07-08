package domain.entity.vos.lock;

import java.time.Instant;

public record RunningDetailsVO(
        Instant expiresAt,
        Instant deadlockNotifiedAt,
        ReleaseDetailsVO releaseDetailsVO
) { }
