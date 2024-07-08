package domain.entity;

import domain.entity.vos.process.ConcurrencyVO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.List;

@AllArgsConstructor
@Getter
public class Process {
    private String name;
    private Integer version;
    private Integer priorityLevel;
    private TemporalAmount lifetime;
    private Boolean isManualDeadlockCleaningRequired;
    private Boolean isStopTheWorld;
    private String notificationTarget;
    private Instant validityStartsAt;
    private Instant validityEndsAt;
    private List<ConcurrencyVO> concurrencies;
}
