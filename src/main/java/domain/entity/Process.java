package domain.entity;

import domain.entity.vos.process.ConcurrencyVO;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class Process {
    private String name;
    private Integer version;
    private Integer priorityLevel;
    private Integer lifetimeInSeconds;
    private Boolean isManualDeadlockCleaningRequired;
    private Boolean isStopTheWorld;
    private String notificationTarget;
    private Instant validityStartsAt;
    private Instant validityEndsAt;
    private List<ConcurrencyVO> concurrencies;
}
