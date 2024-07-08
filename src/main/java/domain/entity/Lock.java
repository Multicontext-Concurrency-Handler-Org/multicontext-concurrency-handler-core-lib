package domain.entity;

import domain.enums.LockStatus;
import domain.entity.vos.lock.RunningDetailsVO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@Getter
public class Lock {
    private String id;
    private Integer version;
    private LockStatus status;
    private Process process;
    private RunningDetailsVO runningDetailsVO;
    private Object context;
    private List<String> workingSet;

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
}
