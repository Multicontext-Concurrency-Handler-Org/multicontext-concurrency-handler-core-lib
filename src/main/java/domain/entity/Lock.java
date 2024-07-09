package domain.entity;

import domain.entity.vos.events.DomainErrorEventVO;
import domain.entity.vos.lock.ReleaseDetailsVO;
import domain.enums.DomainErrorType;
import domain.enums.LockReleaseMode;
import domain.enums.LockStatus;
import domain.entity.vos.lock.RunningDetailsVO;
import domain.event.EventPublisher;
import domain.event.impls.DomainErrorEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.util.*;

@Getter
@AllArgsConstructor
public class Lock {
    private static final Logger logger = LogManager.getLogger();
    private String id;
    private Integer version;
    private LockStatus status;
    private Process process;

    // may be null
    private RunningDetailsVO runningDetailsVO;
    private Object context;
    private List<String> workingSet;

    public Lock(String id, Integer version, Process process, List<String> workingSet, Object context) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(version, "version must not be null");
        Objects.requireNonNull(process, "process must not be null");

        this.id = id;
        this.version = version;
        this.process = process;
        this.status = LockStatus.PENDING;

        this.workingSet = workingSet;
        this.context = context;
        this.runningDetailsVO = null;
    }

    public void startRunning() {
        this.status = LockStatus.RUNNING;
        this.runningDetailsVO = new RunningDetailsVO(
                this.calculateExpiresAt(Instant.now()),
                null,
                null
        );
    }

    private Instant calculateExpiresAt(Instant from) {
        return from.plus(this.process.getLifetime());
    }

    public Optional<Lock> release(LockReleaseMode releaseMode) {
        if(Objects.isNull(this.runningDetailsVO) || !LockStatus.RUNNING.equals(this.status)) {
            EventPublisher.publishEvent(
                    new DomainErrorEvent(
                            new DomainErrorEventVO(
                                    DomainErrorType.INVALID_STATE,
                                    "Can't release a lock that is not running or doesnt have running details",
                                    Instant.now()
                            )
                    )
            );
            return Optional.empty();
        }

        this.status = LockStatus.STOPPED;
        this.runningDetailsVO = new RunningDetailsVO(
                this.runningDetailsVO.expiresAt(),
                this.runningDetailsVO.deadlockNotifiedAt(),
                new ReleaseDetailsVO(
                        Instant.now(),
                        releaseMode
                )
        );
        return Optional.of(this);
    }
}
