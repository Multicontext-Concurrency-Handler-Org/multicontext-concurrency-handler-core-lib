package domain.entity.vos.lock;

import domain.enums.LockReleaseMode;

import java.time.Instant;

public record ReleaseDetailsVO(
        Instant releasedAt,
        LockReleaseMode releaseMode
) { }
